package com.onlinestore.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinestore.main.config.HibernateConfig;
import com.onlinestore.main.config.LiquibaseConfig;
import com.onlinestore.main.controller.config.WebMvcConfig;
import com.onlinestore.main.domain.dto.ProductDto;
import com.onlinestore.main.domain.dto.RegistrationUserDto;
import com.onlinestore.main.domain.dto.WaitingLIstDto;
import com.onlinestore.main.security.WebSecurityConfig;
import com.onlinestore.main.service.IProductService;
import com.onlinestore.main.service.IUserService;
import com.onlinestore.main.service.IWaitingListService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {HibernateConfig.class, LiquibaseConfig.class, WebMvcConfig.class, WebSecurityConfig.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WaitingListControllerTest {

	private static final long PRODUCT_ID = 1;

	private static final String PRODUCT_NAME = "Toy name";

	private static final String USER_NAME = "Alex";

	private static final String PASSWORD = "password";

	private static final String USER_EMAIL = "alex@gmail.com";

	private static final long WAITING_LIST_ID = 1;

	@Autowired
	private IWaitingListService waitingListService;

	@Autowired
	private IProductService productService;

	@Autowired
	private IUserService userService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Before
	public void setUp() {
		init();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).apply((SecurityMockMvcConfigurers.springSecurity())).build();
	}

	@Test
	@WithUserDetails(value = "Alex", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "authService")
	public void testSaveWaitingListWheHttpStatusIsCreated() throws Exception {
		final ProductDto productDto = createProductDto();
		productService.save(productDto);

		mockMvc.perform(post("/waitingLists/{productId}/{username}", PRODUCT_ID, USER_NAME)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}

	@Test
	@WithAnonymousUser
	public void testSaveWaitingListWhenStatusIsUnauthorized() throws Exception {
		final ProductDto productDto = createProductDto();
		productService.save(productDto);

		mockMvc.perform(post("/waitingLists/{productId}/{username}", PRODUCT_ID, USER_NAME)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@WithUserDetails(value = "Alex", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "authService")
	public void testFindByIdHttpStatusIsOk() throws Exception {
		final ProductDto productDto = createProductDto();
		productService.save(productDto);

		final WaitingLIstDto waitingListDto = createWaitingListDto(productDto);
		waitingListService.save(WAITING_LIST_ID, USER_NAME);

		mockMvc.perform(get("/waitingLists/{id}", WAITING_LIST_ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(waitingListDto)))
				.andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser
	public void testFindByIdWhenHttpStatusIsUnauthorized() throws Exception {
		mockMvc.perform(get("/waitingLists/{id}", WAITING_LIST_ID)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	public void testFindAllWenHttpStatusIsOk() throws Exception {
		final ProductDto productDto = createProductDto();
		productService.save(productDto);

		final WaitingLIstDto waitingListDto = createWaitingListDto(productDto);
		waitingListService.save(WAITING_LIST_ID, USER_NAME);

		mockMvc.perform(get("/waitingLists/0/10", WAITING_LIST_ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(waitingListDto)))
				.andExpect(status().isOk());
	}

	@Test
	@WithUserDetails(value = "Alex", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "authService")
	public void testFindAllWenHttpStatusIsForbidden() throws Exception {
		mockMvc.perform(get("/waitingLists/0/10", WAITING_LIST_ID)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	public void testUpdateWhenHttpStatusIsOk() throws Exception {
		final ProductDto productDto = createProductDto();
		productService.save(productDto);

		final WaitingLIstDto waitingListDto = createWaitingListDto(productDto);
		waitingListService.save(WAITING_LIST_ID, USER_NAME);
		waitingListDto.setId(WAITING_LIST_ID);

		mockMvc.perform(put("/waitingLists", waitingListDto)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(waitingListDto)))
				.andExpect(status().isOk());
	}

	@Test
	@WithUserDetails(value = "Alex", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "authService")
	public void testUpdateWhenHttpStatusIsForbidden() throws Exception {
		mockMvc.perform(put("/waitingLists/", WAITING_LIST_ID)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

	private void init() {
		final RegistrationUserDto registrationUserDto = createRegistrationUserDto();
		userService.createNewUser(registrationUserDto);
	}

	private WaitingLIstDto createWaitingListDto(ProductDto productDto) {
		WaitingLIstDto waitingLIstDto = new WaitingLIstDto();
		waitingLIstDto.setUsername(USER_NAME);
		waitingLIstDto.setProductDto(productDto);

		return waitingLIstDto;
	}

	private ProductDto createProductDto() {
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

	private RegistrationUserDto createRegistrationUserDto() {
		RegistrationUserDto registrationUserDto = new RegistrationUserDto();
		registrationUserDto.setName(USER_NAME);
		registrationUserDto.setPassword(PASSWORD);
		registrationUserDto.setConfirmPassword(PASSWORD);
		registrationUserDto.setEmail(USER_EMAIL);

		return registrationUserDto;
	}
}