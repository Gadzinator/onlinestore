package com.onlinestore.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinestore.main.config.HibernateConfig;
import com.onlinestore.main.config.LiquibaseConfig;
import com.onlinestore.main.controller.config.WebMvcConfig;
import com.onlinestore.main.domain.dto.OrderDto;
import com.onlinestore.main.domain.dto.ProductDto;
import com.onlinestore.main.domain.entity.Category;
import com.onlinestore.main.domain.entity.OrderStatus;
import com.onlinestore.main.excepiton.OrderNotFoundException;
import com.onlinestore.main.service.IOrderService;
import com.onlinestore.main.service.IProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {HibernateConfig.class, LiquibaseConfig.class, WebMvcConfig.class})
@WebAppConfiguration
public class OrderControllerTest {

	private static final String PRODUCT_NAME = "Toy name";

	private static final long ORDER_ID = 1;

	private static final long NOT_FOUND_ORDER_ID = 10;

	private static final long PRODUCT_ID = 1;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private IOrderService orderService;

	@Autowired
	private IProductService productService;

	@Before
	public void setUp() {
		init();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	public void addWhenHttpStatusOk() throws Exception {
		final ProductDto productDto = productService.findById(PRODUCT_ID);
		final OrderDto orderDto = createOrderDto(productDto);
		mockMvc.perform(post("/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderDto)))
				.andExpect(status().isCreated());
	}

	@Test
	public void addWhenHttPStatusBadRequest() throws Exception {
		mockMvc.perform(post("/products")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(null)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void findByIdWhenStatusOrderNotFoundException() throws Exception {
		mockMvc.perform(get("/orders/id/{id}", NOT_FOUND_ORDER_ID))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Order not was found with id " + NOT_FOUND_ORDER_ID,
						Objects.requireNonNull(result.getResolvedException()).getMessage()));
	}

	@Test
	public void findAllWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/orders"))
				.andExpect(status().isOk());
	}

	@Test
	public void updateWhenHttpStatusOk() throws Exception {
		final ProductDto productDto = createProductDto();
		productDto.setId(PRODUCT_ID);
		final OrderDto orderDto = createOrderDto(productDto);
		orderDto.setId(ORDER_ID);
		mockMvc.perform(put("/orders", orderDto)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(productDto)))
				.andExpect(status().isOk());
	}

	@Test
	public void updateWhenOrderNotFoundException() throws Exception {
		final ProductDto productDto = createProductDto();
		final OrderDto orderDto = createOrderDto(productDto);
		orderDto.setId(NOT_FOUND_ORDER_ID);
		mockMvc.perform(put("/orders", orderDto)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderDto)))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Order not was found with id " + orderDto.getId(),
						Objects.requireNonNull(result.getResolvedException()).getMessage()));
	}

	@Test
	public void deleteByIdWhenHttpStatusNoContent() throws Exception {
		mockMvc.perform(delete("/orders/{id}", ORDER_ID))
				.andExpect(status().isNoContent());
	}

	@Test
	public void deleteByIdWhenOrderNotFoundException() throws Exception {
		mockMvc.perform(delete("/orders/{id}", ORDER_ID))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertInstanceOf(OrderNotFoundException.class, result.getResolvedException()))
				.andExpect(result -> assertEquals("Order not was found with id " + ORDER_ID,
						Objects.requireNonNull(result.getResolvedException()).getMessage()));
	}

	private void init() {
		final ProductDto productDto = createProductDto();
		productService.add(productDto);
		final ProductDto productById = productService.findById(PRODUCT_ID);
		final OrderDto orderDto = createOrderDto(productById);
		orderService.add(orderDto);
	}

	private OrderDto createOrderDto(ProductDto productDto) {
		List<ProductDto> productDtoList = new ArrayList<>();
		productDtoList.add(productDto);
		OrderDto orderDto = new OrderDto();
		orderDto.setCreated("01-01-2024");
		orderDto.setProducts(productDtoList);
		orderDto.setOrderStatus(OrderStatus.READY.getValue());

		return orderDto;
	}

	private ProductDto createProductDto() {
		ProductDto productDto = new ProductDto();
		productDto.setName(PRODUCT_NAME);
		productDto.setBrand("Toy brand");
		productDto.setDescription("Toy description");
		productDto.setCategory(Category.TOY.getValue());
		productDto.setPrice(100);
		productDto.setCreated("01-11-2024");
		productDto.setAvailable(true);
		productDto.setReceived("01-01-2024");

		return productDto;
	}
}
