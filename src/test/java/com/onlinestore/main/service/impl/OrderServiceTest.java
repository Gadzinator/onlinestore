package com.onlinestore.main.service.impl;

import com.onlinestore.main.domain.dto.OrderDto;
import com.onlinestore.main.domain.dto.ProductDto;
import com.onlinestore.main.domain.entity.Category;
import com.onlinestore.main.domain.entity.Order;
import com.onlinestore.main.domain.entity.OrderStatus;
import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.exception.OrderNotFoundException;
import com.onlinestore.main.mapper.IOrderMapperImpl;
import com.onlinestore.main.repository.impl.OrderRepository;
import com.onlinestore.main.service.impl.config.ServiceTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
public class OrderServiceTest {

	private static final String PRODUCT_NAME = "Toy name";

	private static final long ORDER_ID = 1;

	private static final long PRODUCT_ID = 1;

	private static final int PAGE_NUMBER = 0;

	private static final int PAGE_SIZE = 10;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private IOrderMapperImpl orderMapper;

	@InjectMocks
	private OrderService orderService;

	@Test
	public void testAdd() {
		final ProductDto productDto = createProductDto();
		OrderDto orderDto = createOrderDto(productDto);
		final Product product = createProduct();
		Order order = createOrder(product);

		when(orderMapper.mapToOrder(orderDto)).thenReturn(order);
		doNothing().when(orderRepository).add(order);

		orderService.add(orderDto);

		verify(orderMapper).mapToOrder(orderDto);
		verify(orderRepository).add(order);

		assertEquals(order.getId(), orderDto.getId());
	}

	@Test
	public void testAddWheOrderNull() {
		assertThrows(NullPointerException.class, () -> orderService.add(null));
	}

	@Test
	public void testFindByIdWhenOrderExist() {
		final Product product = createProduct();
		final Order order = createOrder(product);
		final ProductDto productDto = createProductDto();
		final OrderDto orderDto = createOrderDto(productDto);

		when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
		when(orderMapper.mapToOrderDto(order)).thenReturn(orderDto);

		final OrderDto actualeOrderDto = orderService.findById(ORDER_ID);

		verify(orderRepository).findById(ORDER_ID);
		verify(orderMapper).mapToOrderDto(order);
		assertEquals(actualeOrderDto.getId(), order.getId());
	}

	@Test
	public void testFindByIdWhenOrderNotExist() {
		when(orderRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

		assertThrows(OrderNotFoundException.class, () -> orderService.findById(PRODUCT_ID));
	}

	@Test
	public void testFindAllListNotEmpty() {
		final Product product = createProduct();
		final Order firstOrder = createOrder(product);

		final Order secondOrder = createOrder(product);
		secondOrder.setId(2);
		final ProductDto productDto = createProductDto();

		final OrderDto firstOrderDto = createOrderDto(productDto);
		final OrderDto secondOrderDto = createOrderDto(productDto);
		List<Order> orderList = new ArrayList<>();
		orderList.add(firstOrder);
		orderList.add(secondOrder);

		when(orderRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(orderList));
		when(orderMapper.mapToOrderDto(firstOrder)).thenReturn(firstOrderDto);
		when(orderMapper.mapToOrderDto(secondOrder)).thenReturn(secondOrderDto);

		final Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
		final Page<OrderDto> actualOrderDtoList = orderService.findAll(pageable);

		verify(orderRepository).findAll(any(Pageable.class));
		verify(orderMapper, times(1)).mapToOrderDto(firstOrder);
		verify(orderMapper, times(1)).mapToOrderDto(secondOrder);

		assertFalse(actualOrderDtoList.isEmpty());
		assertEquals(2, actualOrderDtoList.getSize());
		assertTrue(actualOrderDtoList.getContent().contains(firstOrderDto));
		assertTrue(actualOrderDtoList.getContent().contains(secondOrderDto));
	}

	@Test
	public void testFindAllWhenOrdersNotExist() {
		final Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

		when(orderRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

		assertThrows(OrderNotFoundException.class, () -> orderService.findAll(pageable));
	}

	@Test
	public void testUpdateByIdWhenOrderExists() {
		final Product product = createProduct();
		final Order order = createOrder(product);
		order.setOrderStatus(OrderStatus.IN_PROGRESS);

		final ProductDto productDto = createProductDto();
		final OrderDto orderDto = createOrderDto(productDto);
		orderDto.setOrderStatus(OrderStatus.IN_PROGRESS.getValue());

		when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
		when(orderMapper.mapToOrderDto(any(Order.class))).thenReturn(orderDto);
		when(orderMapper.mapToOrder(any(OrderDto.class))).thenReturn(order);
		doNothing().when(orderRepository).update(any(Order.class));

		orderService.update(orderDto);

		final ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);

		verify(orderRepository, times(1)).findById(order.getId());
		verify(orderMapper, times(1)).mapToOrder(orderDto);
		verify(orderRepository, times(1)).update(orderArgumentCaptor.capture());
		final Order savedOrder = orderArgumentCaptor.getValue();

		assertEquals(OrderStatus.IN_PROGRESS, savedOrder.getOrderStatus());
	}

	@Test
	public void testUpdateByIdWhenOrderDoesNotExist() {
		final ProductDto productDto = createProductDto();
		OrderDto orderDtoUpdate = createOrderDto(productDto);
		when(orderRepository.findById(orderDtoUpdate.getId())).thenReturn(Optional.empty());

		assertThrows(OrderNotFoundException.class, () -> orderService.update(orderDtoUpdate));
		verify(orderRepository, times(1)).findById(orderDtoUpdate.getId());
		verify(orderRepository, never()).update(any());
	}


	@Test
	public void deleteByIDWhenOrderExist() {
		final Product product = createProduct();
		final Order order = createOrder(product);

		final ProductDto productDto = createProductDto();
		final OrderDto orderDto = createOrderDto(productDto);

		when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
		when(orderMapper.mapToOrderDto(order)).thenReturn(orderDto);

		orderService.deleteById(ORDER_ID);

		verify(orderRepository).findById(ORDER_ID);
		verify(orderMapper).mapToOrderDto(order);
	}

	@Test
	public void deleteByIDWhenOrderNotExist() {
		when(orderRepository.findById(ORDER_ID)).thenThrow(OrderNotFoundException.class);

		assertThrows(OrderNotFoundException.class, () -> orderService.deleteById(ORDER_ID));
		verify(orderRepository, never()).deleteById(PRODUCT_ID);
	}

	@Test
	public void testFindProductsOrderIdWhenOrderExist() {
		final Product product = createProduct();
		final Order order = createOrder(product);

		final ProductDto productDto = createProductDto();
		final OrderDto orderDto = createOrderDto(productDto);
		List<Product> productList = new ArrayList<>();
		productList.add(product);

		when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
		when(orderMapper.mapToOrderDto(order)).thenReturn(orderDto);
		when(orderRepository.findProductsOrderId(ORDER_ID)).thenReturn(List.of(product));

		final List<Product> foundProductList = orderService.findProductsByOrderId(ORDER_ID);

		assertEquals(productList.size(), foundProductList.size());
		assertEquals(productList, foundProductList);
	}

	@Test
	public void testFindProductsOrderIdWhenOrderNotExist() {
		assertThrows(OrderNotFoundException.class, () -> orderService.findProductsByOrderId(ORDER_ID));
	}

	private Order createOrder(Product product) {
		List<Product> products = new ArrayList<>();
		products.add(product);
		Order order = new Order();
		order.setId(ORDER_ID);
		order.setProducts(products);
		order.setCreated(LocalDate.now().plusDays(10));
		order.setOrderStatus(OrderStatus.NOT_READY);

		return order;
	}

	private OrderDto createOrderDto(ProductDto productDto) {
		List<ProductDto> productDtoList = new ArrayList<>();
		productDtoList.add(productDto);
		OrderDto orderDto = new OrderDto();
		orderDto.setId(ORDER_ID);
		orderDto.setCreated("01-01-2024");
		orderDto.setProducts(productDtoList);
		orderDto.setOrderStatus(OrderStatus.READY.getValue());

		return orderDto;
	}

	private Product createProduct() {
		Category category = new Category();
		category.setName("TOY");
		Product product = new Product();
		product.setId(PRODUCT_ID);
		product.setName(PRODUCT_NAME);
		product.setBrand("Toy brand");
		product.setDescription("Toy description");
		product.setCategory(category);
		product.setPrice(100);
		product.setCreated(LocalDate.now());
		product.setAvailable(true);
		product.setReceived(LocalDate.now().plusDays(3));

		return product;
	}

	private ProductDto createProductDto() {
		ProductDto productDto = new ProductDto();
		productDto.setId(PRODUCT_ID);
		productDto.setName(PRODUCT_NAME);
		productDto.setBrand("Toy brand");
		productDto.setDescription("Toy description");
		productDto.setCategory("TOY");
		productDto.setPrice(100);
		productDto.setCreated("01-11-2024");
		productDto.setAvailable(true);
		productDto.setReceived("01-01-2024");

		return productDto;
	}
}
