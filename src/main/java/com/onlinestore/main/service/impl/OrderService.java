package com.onlinestore.main.service.impl;

import com.onlinestore.main.domain.dto.OrderDto;
import com.onlinestore.main.domain.entity.Order;
import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.exception.OrderNotFoundException;
import com.onlinestore.main.mapper.IOrderMapper;
import com.onlinestore.main.repository.impl.OrderRepository;
import com.onlinestore.main.service.IOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@AllArgsConstructor
@Service
public class OrderService implements IOrderService {

	private OrderRepository orderRepository;

	private IOrderMapper orderMapper;

	@Transactional
	@Override
	public void add(OrderDto orderDto) {
		log.info("Start adding an order: " + orderDto);

		if (orderDto == null) {
			throw new NullPointerException("ProductDto cannot be null");
		}

		orderRepository.add(orderMapper.mapToOrder(orderDto));

		log.info("Finishing adding an order " + orderDto);
	}

	@Override
	public OrderDto findById(long id) {
		log.info("Start finding order by id: " + id);

		final OrderDto orderDto = orderRepository.findById(id).map(order -> orderMapper.mapToOrderDto(order)).orElseThrow(
				() -> new OrderNotFoundException("Order not was found by id " + id));

		log.info("Finishing finding order by id: " + orderDto);

		return orderDto;
	}

	@Transactional
	@Override
	public Page<OrderDto> findAll(Pageable pageable) {
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
	public void update(OrderDto orderDtoUpdate) {
		log.info("Starting updating order: " + orderDtoUpdate);
		final OrderDto orderDto = findById(orderDtoUpdate.getId());
		if (orderDto != null) {
			final Order order = orderMapper.mapToOrder(orderDto);
			orderRepository.update(order);

			log.info("Finished updated successfully: " + order);
		} else {
			throw new OrderNotFoundException("Order not was found by id " + orderDtoUpdate.getId());
		}
	}

	@Transactional
	@Override
	public void deleteById(long id) {
		log.info("Starting delete order by id: " + id);

		final OrderDto orderDto = findById(id);
		if (orderDto != null) {
			orderRepository.deleteById(orderDto.getId());

			log.info("Finished deleting order by id");
		} else {
			throw new OrderNotFoundException("Order not was found by id " + id);
		}
	}

	@Override
	public List<Product> findProductsByOrderId(long id) {
		log.info("Starting finding products by order id: " + id);

		final OrderDto orderDto = findById(id);
		if (orderDto != null) {
			final List<Product> productList = orderRepository.findProductsOrderId(orderDto.getId());

			log.info("Finished finding products by order id: " + productList);

			return productList;
		} else {
			throw new OrderNotFoundException("Order not was found by id " + id);
		}
	}
}
