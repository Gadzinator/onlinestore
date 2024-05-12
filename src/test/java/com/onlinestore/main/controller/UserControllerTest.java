package com.onlinestore.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinestore.main.config.HibernateConfig;
import com.onlinestore.main.config.LiquibaseConfig;
import com.onlinestore.main.controller.config.WebMvcConfig;
import com.onlinestore.main.domain.dto.RegistrationUserDto;
import com.onlinestore.main.domain.dto.UserDto;
import com.onlinestore.main.exception.UserNotFoundException;
import com.onlinestore.main.security.WebSecurityConfig;
import com.onlinestore.main.service.IUserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
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

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {HibernateConfig.class, LiquibaseConfig.class, WebMvcConfig.class, WebSecurityConfig.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest {

	private static final long USER_ID = 1L;

	private static final long NOT_FIND_USER_ID = 10;

	private static final String USER_NAME = "Alex";

	private static final String PASSWORD = "passwordAlex";

	private static final String USER_EMAIL = "alex@gmail.com";

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private IUserService userService;

	@Before
	public void setUp() {
		init();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).apply((SecurityMockMvcConfigurers.springSecurity())).build();
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	public void testFindByIdWhenStatusOk() throws Exception {
		final UserDto userDto = createUserDto();

		mockMvc.perform(get("/users/id/{id}", USER_ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userDto)))
				.andExpect(status().isOk());
	}

	@Test
	@WithUserDetails(value = "Alex", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "authService")
	public void testFindByIdWhenStatusIsForbidden() throws Exception {
		final UserDto userDto = createUserDto();

		mockMvc.perform(get("/users/id/{id}", USER_ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userDto)))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	public void testFindByIdWhenStatusUserNotFoundException() throws Exception {
		mockMvc.perform(get("/users/id/{id}", NOT_FIND_USER_ID))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("User was not found by id " + NOT_FIND_USER_ID,
						Objects.requireNonNull(result.getResolvedException()).getMessage()));
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	public void testFindAllWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/users/0/10"))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	public void testFindByNameWhenHttpStatusOk() throws Exception {
		final UserDto userDto = createUserDto();

		mockMvc.perform(get("/users/name/{name}", USER_NAME)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userDto)))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	public void testFindByNameWhenUserNotFoundException() throws Exception {
		mockMvc.perform(get("/users/name/{name}", "Petro"))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertInstanceOf(UserNotFoundException.class, result.getResolvedException()))
				.andExpect(result -> assertEquals("User was not found by name " + "Petro",
						Objects.requireNonNull(result.getResolvedException()).getMessage()));
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	public void deleteByIdWhenHttpStatusNoContent() throws Exception {
		mockMvc.perform(delete("/users/{id}", USER_ID))
				.andExpect(status().isNoContent());
	}

	private void init() {
		final RegistrationUserDto registrationUserDto = createRegistrationUserDto();
		userService.createNewUser(registrationUserDto);
	}

	private RegistrationUserDto createRegistrationUserDto() {
		RegistrationUserDto registrationUserDto = new RegistrationUserDto();
		registrationUserDto.setName(USER_NAME);
		registrationUserDto.setPassword(PASSWORD);
		registrationUserDto.setConfirmPassword(PASSWORD);
		registrationUserDto.setEmail(USER_EMAIL);

		return registrationUserDto;
	}

	private UserDto createUserDto() {
		UserDto userDto = new UserDto();
		userDto.setName(USER_NAME);
		userDto.setEmail(USER_EMAIL);

		return userDto;
	}
}
