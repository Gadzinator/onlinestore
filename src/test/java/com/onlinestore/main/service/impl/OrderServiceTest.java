package com.onlinestore.main.service.impl;

import com.onlinestore.main.domain.dto.OrderRequestDto;
import com.onlinestore.main.domain.dto.OrderResponseDto;
import com.onlinestore.main.domain.dto.ProductDto;
import com.onlinestore.main.domain.dto.UserDto;
import com.onlinestore.main.domain.entity.Category;
import com.onlinestore.main.domain.entity.Order;
import com.onlinestore.main.domain.entity.OrderStatus;
import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.domain.entity.User;
import com.onlinestore.main.exception.OrderNotFoundException;
import com.onlinestore.main.exception.UserNotFoundException;
import com.onlinestore.main.mapper.IOrderMapperImpl;
import com.onlinestore.main.mapper.IOrderResponseMapperImpl;
import com.onlinestore.main.mapper.IProductMapperImpl;
import com.onlinestore.main.mapper.IUserMapperImpl;
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

import static com.onlinestore.main.domain.entity.Role.ROLE_USER;
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

	private static final long USER_ID = 1;

	private static final String USER_NAME = "Alex";

	private static final String USER_EMAIL = "alex@gmail.ru";

	private static final String PASSWORD = "Alex";

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private IOrderMapperImpl orderMapper;

	@Mock
	private IUserMapperImpl userMapper;

	@Mock
	private IProductMapperImpl productMapper;

	@Mock
	private IOrderResponseMapperImpl orderResponseMapper;

	@Mock
	private UserService userService;

	@Mock
	private ProductService productService;

	@InjectMocks
	private OrderService orderService;

	@Test
	public void testSave() {
		// given
		final ProductDto productDto = createProductDto();
		final Product product = createProduct();
		Order order = createOrder(product);

		final User user = createUser();
		final UserDto userDto = createUserDto();
		final OrderRequestDto orderRequestDto = createOrderRequestDto(productDto);

		when(userService.findById(orderRequestDto.getUserId())).thenReturn(userDto);
		when(userMapper.mapToUser(userDto)).thenReturn(user);
		when(orderResponseMapper.mapToOrder(orderRequestDto)).thenReturn(order);
		doNothing().when(orderRepository).save(order);

		// when
		orderService.save(orderRequestDto);

		// then
		verify(userService).findById(USER_ID);
		verify(userMapper).mapToUser(userDto);
		verify(orderResponseMapper).mapToOrder(orderRequestDto);

		assertEquals(order.getId(), orderRequestDto.getId());
	}

	@Test
	public void testSaveWheUserNotFoundException() {
		// given
		final ProductDto productDto = createProductDto();
		final OrderRequestDto orderRequestDto = createOrderRequestDto(productDto);

		when(userService.findById(USER_ID)).thenReturn(null);

		// when and then
		assertThrows(UserNotFoundException.class, () -> orderService.save(orderRequestDto));

		verify(userService).findById(USER_ID);
	}

	@Test
	public void testFindByIdWhenOrderExist() {
		// given
		final Product product = createProduct();
		final Order order = createOrder(product);
		final ProductDto productDto = createProductDto();
		final OrderResponseDto orderResponseDto = createOrderDto(productDto);

		final UserDto userDto = createUserDto();

		when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
		when(orderMapper.mapToOrderDto(order)).thenReturn(orderResponseDto);
		when(userService.findById(USER_ID)).thenReturn(userDto);

		// when
		final OrderResponseDto actualeOrderResponseDto = orderService.findById(ORDER_ID);

		// then
		verify(orderRepository).findById(ORDER_ID);
		verify(orderMapper).mapToOrderDto(order);
		verify(userService).findById(USER_ID);
		assertEquals(actualeOrderResponseDto.getId(), order.getId());
	}

	@Test
	public void testFindByIdWhenOrderNotExist() {
		// given
		when(orderRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

		// when and then
		assertThrows(OrderNotFoundException.class, () -> orderService.findById(PRODUCT_ID));

		verify(orderRepository).findById(PRODUCT_ID);
	}

	@Test
	public void testFindAllListNotEmpty() {
		// given
		final Product product = createProduct();
		final Order firstOrder = createOrder(product);

		final Order secondOrder = createOrder(product);
		secondOrder.setId(2);
		final ProductDto productDto = createProductDto();

		final OrderResponseDto firstOrderResponseDto = createOrderDto(productDto);
		final OrderResponseDto secondOrderResponseDto = createOrderDto(productDto);
		List<Order> orderList = new ArrayList<>();
		orderList.add(firstOrder);
		orderList.add(secondOrder);

		when(orderRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(orderList));
		when(orderMapper.mapToOrderDto(firstOrder)).thenReturn(firstOrderResponseDto);
		when(orderMapper.mapToOrderDto(secondOrder)).thenReturn(secondOrderResponseDto);

		final Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

		// when
		final Page<OrderResponseDto> actualOrderDtoList = orderService.findAll(pageable);

		// then
		verify(orderRepository).findAll(any(Pageable.class));
		verify(orderMapper, times(1)).mapToOrderDto(firstOrder);
		verify(orderMapper, times(1)).mapToOrderDto(secondOrder);

		assertFalse(actualOrderDtoList.isEmpty());
		assertEquals(2, actualOrderDtoList.getSize());
		assertTrue(actualOrderDtoList.getContent().contains(firstOrderResponseDto));
		assertTrue(actualOrderDtoList.getContent().contains(secondOrderResponseDto));
	}

	@Test
	public void testFindAllWhenOrdersNotExist() {
		// given
		final Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

		when(orderRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

		// when and then
		assertThrows(OrderNotFoundException.class, () -> orderService.findAll(pageable));

		verify(orderRepository).findAll(pageable);
	}

	@Test
	public void testUpdateByIdWhenOrderExists() {
		// given
		final Product product = createProduct();
		final Order order = createOrder(product);
		order.setOrderStatus(OrderStatus.IN_PROGRESS);

		final UserDto userDto = createUserDto();

		final ProductDto productDto = createProductDto();

		final OrderRequestDto orderRequestDto = createOrderRequestDto(productDto);
		orderRequestDto.setOrderStatus(OrderStatus.IN_PROGRESS);

		final OrderResponseDto orderResponseDto = createOrderDto(productDto);
		orderResponseDto.setOrderStatus(OrderStatus.IN_PROGRESS.getValue());

		when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
		when(orderMapper.mapToOrder(any(OrderResponseDto.class))).thenReturn(order);
		when(orderMapper.mapToOrderDto(any(Order.class))).thenReturn(orderResponseDto);
		when(productService.findById(PRODUCT_ID)).thenReturn(productDto);
		when(productMapper.mapToProduct(any(ProductDto.class))).thenReturn(product);
		when(userService.findById(USER_ID)).thenReturn(userDto);
		doNothing().when(orderRepository).update(any(Order.class));

		// when
		orderService.update(orderRequestDto);

		// then
		final ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);

		verify(orderRepository).findById(order.getId());
		verify(orderMapper).mapToOrder(orderResponseDto);
		verify(productService).findById(PRODUCT_ID);
		verify(productMapper).mapToProduct(productDto);
		verify(userService).findById(USER_ID);
		verify(orderRepository).update(orderArgumentCaptor.capture());

		final Order savedOrder = orderArgumentCaptor.getValue();
		assertEquals(OrderStatus.IN_PROGRESS, savedOrder.getOrderStatus());
	}

	@Test
	public void testUpdateByIdWhenOrderDoesNotExist() {
		// given
		final ProductDto productDto = createProductDto();
		OrderResponseDto orderResponseDtoUpdate = createOrderDto(productDto);

		final OrderRequestDto orderRequestDto = createOrderRequestDto(productDto);

		when(orderRepository.findById(orderResponseDtoUpdate.getId())).thenReturn(Optional.empty());

		// when and then
		assertThrows(OrderNotFoundException.class, () -> orderService.update(orderRequestDto));

		verify(orderRepository, times(1)).findById(orderResponseDtoUpdate.getId());
		verify(orderRepository, never()).update(any());
	}


	@Test
	public void deleteByIDWhenOrderExist() {
		final Product product = createProduct();
		final Order order = createOrder(product);

		final UserDto userDto = createUserDto();

		final ProductDto productDto = createProductDto();
		final OrderResponseDto orderResponseDto = createOrderDto(productDto);

		when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
		when(orderMapper.mapToOrderDto(order)).thenReturn(orderResponseDto);
		when(userService.findById(USER_ID)).thenReturn(userDto);

		orderService.deleteById(ORDER_ID);

		verify(orderRepository).findById(ORDER_ID);
		verify(orderMapper).mapToOrderDto(order);
		verify(userService).findById(USER_ID);
	}

	@Test
	public void deleteByIDWhenOrderNotExist() {
		// given
		when(orderRepository.findById(ORDER_ID)).thenThrow(OrderNotFoundException.class);

		// when and then
		assertThrows(OrderNotFoundException.class, () -> orderService.deleteById(ORDER_ID));

		verify(orderRepository, never()).deleteById(PRODUCT_ID);
	}

	@Test
	public void testFindProductsOrderIdWhenOrderExist() {
		// given
		final Product product = createProduct();
		final Order order = createOrder(product);

		final UserDto userDto = createUserDto();

		final ProductDto productDto = createProductDto();
		final OrderResponseDto orderResponseDto = createOrderDto(productDto);
		List<Product> productList = new ArrayList<>();
		productList.add(product);

		when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
		when(orderMapper.mapToOrderDto(order)).thenReturn(orderResponseDto);
		when(userService.findById(USER_ID)).thenReturn(userDto);
		when(orderRepository.findProductsOrderId(ORDER_ID)).thenReturn(List.of(product));

		// when
		final List<Product> foundProductList = orderService.findProductsByOrderId(ORDER_ID);

		// then
		assertEquals(productList.size(), foundProductList.size());
		assertEquals(productList, foundProductList);
	}

	@Test
	public void testFindProductsOrderIdWhenOrderNotExist() {
		// when and then
		assertThrows(OrderNotFoundException.class, () -> orderService.findProductsByOrderId(ORDER_ID));
	}

	private Order createOrder(Product product) {
		List<Product> products = new ArrayList<>();
		products.add(product);
		final User user = createUser();
		Order order = new Order();

		order.setId(ORDER_ID);
		order.setUser(user);
		order.setProducts(products);
		order.setCreated(LocalDate.now().plusDays(10));
		order.setOrderStatus(OrderStatus.NOT_READY);

		return order;
	}

	private OrderRequestDto createOrderRequestDto(ProductDto productDto) {
		List<Long> productsIds = new ArrayList<>();
		productsIds.add(productDto.getId());

		OrderRequestDto orderRequestDto = new OrderRequestDto();
		orderRequestDto.setId(ORDER_ID);
		orderRequestDto.setUserId(USER_ID);
		orderRequestDto.setCreated("01-11-2024");
		orderRequestDto.setProductIds(productsIds);
		orderRequestDto.setOrderStatus(OrderStatus.IN_PROGRESS);

		return orderRequestDto;
	}

	private OrderResponseDto createOrderDto(ProductDto productDto) {
		List<ProductDto> productDtoList = new ArrayList<>();
		productDtoList.add(productDto);

		OrderResponseDto orderResponseDto = new OrderResponseDto();
		orderResponseDto.setId(ORDER_ID);
		orderResponseDto.setUserId(USER_ID);
		orderResponseDto.setCreated("01-01-2024");
		orderResponseDto.setProducts(productDtoList);
		orderResponseDto.setOrderStatus(OrderStatus.READY.getValue());

		return orderResponseDto;
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

	private User createUser() {
		User user = new User();
		user.setId(USER_ID);
		user.setName(USER_NAME);
		user.setRole(ROLE_USER);
		user.setPassword(PASSWORD);
		user.setEmail(USER_EMAIL);

		return user;
	}

	private UserDto createUserDto() {
		UserDto userDto = new UserDto();
		userDto.setId(USER_ID);
		userDto.setName(USER_NAME);
		userDto.setEmail(USER_EMAIL);

		return userDto;
	}
}
