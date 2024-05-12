package com.onlinestore.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinestore.main.config.HibernateConfig;
import com.onlinestore.main.config.LiquibaseConfig;
import com.onlinestore.main.controller.config.WebMvcConfig;
import com.onlinestore.main.domain.dto.CategoryDto;
import com.onlinestore.main.domain.dto.ProductDto;
import com.onlinestore.main.exception.ProductNotFoundException;
import com.onlinestore.main.security.WebSecurityConfig;
import com.onlinestore.main.service.IProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {HibernateConfig.class, LiquibaseConfig.class, WebMvcConfig.class, WebSecurityConfig.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProductControllerTest {

	private static final String PRODUCT_NAME = "Toy name";

	private static final long PRODUCT_ID = 1;

	private static final long NOT_FOUND_PRODUCT_ID = 10;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private IProductService productService;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).apply((SecurityMockMvcConfigurers.springSecurity())).build();
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	public void addWhenHttpStatusOk() throws Exception {
		final ProductDto productDto = createProductDto();
		mockMvc.perform(post("/products")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(productDto)))
				.andExpect(status().isCreated());
	}

	@Test
	@WithAnonymousUser
	public void addWhenHttpIsUnauthorized() throws Exception {
		final ProductDto productDto = createProductDto();
		mockMvc.perform(post("/products")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(productDto)))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	public void addWhenHttPStatusBadRequest() throws Exception {
		mockMvc.perform(post("/products")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(null)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void findAllWhenHttpStatusOk() throws Exception {
		final ProductDto productDto = createProductDto();
		productService.add(productDto);
		mockMvc.perform(get("/products/0/10"))
				.andExpect(status().isOk());
	}

	@Test
	public void findByIdWhenStatusProductNotFoundException() throws Exception {
		final ProductDto productDto = createProductDto();
		productService.add(productDto);
		mockMvc.perform(get("/products/id/{id}", NOT_FOUND_PRODUCT_ID))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Product not was found by id " + NOT_FOUND_PRODUCT_ID,
						Objects.requireNonNull(result.getResolvedException()).getMessage()));
	}

	@Test
	public void findByIdWhenStatusOk() throws Exception {
		final ProductDto productDto = createProductDto();
		productService.add(productDto);
		mockMvc.perform(get("/products/id/{id}", PRODUCT_ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(productDto)))
				.andExpect(status().isOk());
	}

	@Test
	public void findByNameWhenHttpStatusOk() throws Exception {
		final ProductDto productDto = createProductDto();
		productDto.setName("Product");
		productService.add(productDto);
		mockMvc.perform(get("/products/name/{name}", "Product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(productDto)))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	public void updateWhenHttpStatusOk() throws Exception {
		final ProductDto productDto = createProductDto();
		productService.add(productDto);
		productDto.setId(PRODUCT_ID);
		mockMvc.perform(put("/products", productDto)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(productDto)))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	public void updateWhenProductNotFoundException() throws Exception {
		final ProductDto productDto = createProductDto();
		productDto.setId(100);
		mockMvc.perform(put("/products", productDto)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(productDto)))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Product not was found by id " + 100,
						Objects.requireNonNull(result.getResolvedException()).getMessage()));
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	public void deleteByIdWhenHttpStatusNoContent() throws Exception {
		final ProductDto productDto = createProductDto();
		productService.add(productDto);
		mockMvc.perform(delete("/products/{id}", PRODUCT_ID))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	public void deleteByIdWhenProductNotFoundException() throws Exception {
		final ProductDto productDto = createProductDto();
		productService.add(productDto);
		mockMvc.perform(delete("/products/{id}", NOT_FOUND_PRODUCT_ID))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertInstanceOf(ProductNotFoundException.class, result.getResolvedException()))
				.andExpect(result -> assertEquals("Product not was found by id " + NOT_FOUND_PRODUCT_ID,
						Objects.requireNonNull(result.getResolvedException()).getMessage()));
	}

	private ProductDto createProductDto() {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setId(1);
		ProductDto productDto = new ProductDto();
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
