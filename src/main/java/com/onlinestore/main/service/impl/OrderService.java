package com.onlinestore.main.service.impl;

import com.onlinestore.main.domain.dto.OrderRequestDto;
import com.onlinestore.main.domain.dto.OrderResponseDto;
import com.onlinestore.main.domain.dto.ProductDto;
import com.onlinestore.main.domain.dto.UserDto;
import com.onlinestore.main.domain.entity.Order;
import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.domain.entity.User;
import com.onlinestore.main.exception.OrderNotFoundException;
import com.onlinestore.main.exception.UserNotFoundException;
import com.onlinestore.main.mapper.IOrderMapper;
import com.onlinestore.main.mapper.IOrderResponseMapper;
import com.onlinestore.main.mapper.IProductMapper;
import com.onlinestore.main.mapper.IUserMapper;
import com.onlinestore.main.repository.impl.OrderRepository;
import com.onlinestore.main.service.IOrderService;
import com.onlinestore.main.service.IProductService;
import com.onlinestore.main.service.IUserService;
import com.onlinestore.main.utils.DateConstant;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@AllArgsConstructor
@Service
public class OrderService implements IOrderService {

	private IUserService userService;

	private OrderRepository orderRepository;

	private IProductService productService;

	private IProductMapper productMapper;

	private IOrderMapper orderMapper;

	private IUserMapper userMapper;

	private IOrderResponseMapper orderResponseMapper;

	@Transactional
	@Override
	public void save(OrderRequestDto orderRequestDto) {
		log.info("Start adding an order: " + orderRequestDto);

		final UserDto userDto = userService.findById(orderRequestDto.getUserId());
		if (userDto == null) {
			throw new UserNotFoundException("User was not found by id " + orderRequestDto.getUserId());
		}

		final User user = userMapper.mapToUser(userDto);
		final Order order = orderResponseMapper.mapToOrder(orderRequestDto);
		order.setUser(user);

		orderRepository.save(order);

		log.info("Finishing adding an order " + order);
	}

	@Transactional
	@Override
	public OrderResponseDto findById(long id) {
		log.info("Start finding order by id: " + id);

		final OrderResponseDto orderResponseDto = orderRepository.findById(id).map(order -> orderMapper.mapToOrderDto(order)).orElseThrow(
				() -> new OrderNotFoundException("Order not was found by id " + id));

		final UserDto userDto = userService.findById(orderResponseDto.getUserId());
		final int totalPrice = getTotalPrice(orderResponseDto.getProducts());

		orderResponseDto.setTotalPrice(totalPrice);
		orderResponseDto.setUserId(userDto.getId());

		log.info("Finishing finding order by id: " + orderResponseDto);

		return orderResponseDto;
	}

	@Transactional
	@Override
	public Page<OrderResponseDto> findAll(Pageable pageable) {
		log.info("Start finding all orders:");

		Page<Order> ordersPage = orderRepository.findAll(pageable);
		if (ordersPage.isEmpty()) {
			throw new OrderNotFoundException("Orders were not found");
		}

		log.info("Finished finding all orders: " + ordersPage);

		return ordersPage.map(orderMapper::mapToOrderDto);
	}

	@Transactional
	@Override
	public void update(OrderRequestDto orderRequestDto) {
		log.info("Starting updating order: " + orderRequestDto);

		final OrderResponseDto orderResponseDto = findById(orderRequestDto.getId());
		if (orderResponseDto != null) {

			final Order order = orderMapper.mapToOrder(orderResponseDto);
			updateAllFields(order, orderRequestDto);

			orderRepository.update(order);

			log.info("Finished updated successfully: " + order);

		} else {
			throw new OrderNotFoundException("Order not was found by id " + orderRequestDto.getId());
		}
	}

	@Transactional
	@Override
	public void deleteById(long id) {
		log.info("Starting delete order by id: " + id);

		final OrderResponseDto orderResponseDto = findById(id);
		if (orderResponseDto != null) {
			orderRepository.deleteById(orderResponseDto.getId());

			log.info("Finished deleting order by id");
		} else {
			throw new OrderNotFoundException("Order not was found by id " + id);
		}
	}

	@Transactional
	@Override
	public List<Product> findProductsByOrderId(long id) {
		log.info("Starting finding products by order id: " + id);

		final OrderResponseDto orderResponseDto = findById(id);
		if (orderResponseDto != null) {
			final List<Product> productList = orderRepository.findProductsOrderId(orderResponseDto.getId());

			log.info("Finished finding products by order id: " + productList);

			return productList;
		} else {
			throw new OrderNotFoundException("Order not was found by id " + id);
		}
	}

	private int getTotalPrice(List<ProductDto> dtoList) {
		return dtoList.stream()
				.mapToInt(ProductDto::getPrice)
				.sum();
	}

	private List<Product> getListProducts(OrderRequestDto orderRequestDto) {
		log.info("Starting getting list products: " + orderRequestDto);

		List<Product> products = new ArrayList<>();
		final List<Long> productIds = orderRequestDto.getProductIds();
		for (Long productId : productIds) {
			final ProductDto productDto = productService.findById(productId);
			final Product product = productMapper.mapToProduct(productDto);
			products.add(product);
		}

		log.info("Finished getting list products: " + products);

		return products;
	}

	private void updateAllFields(Order order, OrderRequestDto orderRequestDto) {
		log.info("Starting updating all fields: " + order);

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateConstant.DEFAULT_DATE_PATTERN);
		final List<Product> products = getListProducts(orderRequestDto);

		order.setCreated(LocalDate.parse(orderRequestDto.getCreated(), dateTimeFormatter));
		order.setProducts(products);
		order.setOrderStatus(orderRequestDto.getOrderStatus());

		log.info("Finished updating all fields: " + order);
	}
}
