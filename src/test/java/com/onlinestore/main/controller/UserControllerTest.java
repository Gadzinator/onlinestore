package com.onlinestore.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinestore.main.config.HibernateConfig;
import com.onlinestore.main.config.LiquibaseConfig;
import com.onlinestore.main.controller.config.WebMvcConfig;
import com.onlinestore.main.domain.dto.RegistrationUserDto;
import com.onlinestore.main.domain.dto.UserDto;
import com.onlinestore.main.excepiton.UserNotFoundException;
import com.onlinestore.main.service.IUserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {HibernateConfig.class, LiquibaseConfig.class, WebMvcConfig.class})
@WebAppConfiguration
public class UserControllerTest {

	private static final long USER_ID = 1L;

	private static final long NOT_FIND_USER_ID = 10;

	private static final long FOUND_USER_ID = 2;

	private static final String USER_NAME = "Alex";


	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private IUserService userService;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	public void testCreateNewUserWhenStatusCreated() throws Exception {
		RegistrationUserDto registrationUserDto = createRegistrationUserDto();
		registrationUserDto.setName(USER_NAME);
		mockMvc.perform(post("/users/registration")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registrationUserDto)))
				.andExpect(status().isCreated());
	}

	@Test
	public void testCreateNewUserWhenStatusBadRequest() throws Exception {
		mockMvc.perform(post("/users/registration")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(null)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void findByIdWhenStatusOk() throws Exception {
		final UserDto userDto = createUserDto();
		mockMvc.perform(get("/users/id/{id}", FOUND_USER_ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userDto)))
				.andExpect(status().isOk());
	}

	@Test
	public void findByIdWhenStatusUserNotFoundException() throws Exception {
		mockMvc.perform(get("/users/id/{id}", NOT_FIND_USER_ID))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("User was not found with id " + NOT_FIND_USER_ID,
						Objects.requireNonNull(result.getResolvedException()).getMessage()));
	}

	@Test
	public void findAllWhenHttpStatusOk() throws Exception {
		RegistrationUserDto registrationUserDto = createRegistrationUserDto();
		userService.createNewUser(registrationUserDto);
		mockMvc.perform(get("/users"))
				.andExpect(status().isOk());
	}

	@Test
	public void findAllWhenHttpStatusUserNotFoundException() throws Exception {
		mockMvc.perform(get("/users"))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertInstanceOf(UserNotFoundException.class, result.getResolvedException()))
				.andExpect(result -> assertEquals("Users were not found", Objects.requireNonNull(result.getResolvedException()).getMessage()));
	}

	@Test
	public void findByNameWhenHttpStatusOk() throws Exception {
		RegistrationUserDto registrationUserDto = createRegistrationUserDto();
		registrationUserDto.setName(USER_NAME);
		userService.createNewUser(registrationUserDto);
		final UserDto userDto = createUserDto();
		mockMvc.perform(get("/users/name/{name}", USER_NAME)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userDto)))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk());
	}

	@Test
	public void findByNameWhenUserNotFoundException() throws Exception {
		RegistrationUserDto registrationUserDto = createRegistrationUserDto();
		userService.createNewUser(registrationUserDto);
		mockMvc.perform(get("/users/name/{name}", "Petro"))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertInstanceOf(UserNotFoundException.class, result.getResolvedException()))
				.andExpect(result -> assertEquals("User was not found with name " + "Petro",
						Objects.requireNonNull(result.getResolvedException()).getMessage()));
	}

	@Test
	public void deleteByIdWhenHttpStatusNoContent() throws Exception {
		RegistrationUserDto registrationUserDto = createRegistrationUserDto();
		;
		userService.createNewUser(registrationUserDto);
		mockMvc.perform(delete("/users/{id}", USER_ID))
				.andExpect(status().isNoContent());
	}


	private void init() {
		RegistrationUserDto registrationUserDto = createRegistrationUserDto();
		userService.createNewUser(registrationUserDto);
	}

	private RegistrationUserDto createRegistrationUserDto() {
		RegistrationUserDto registrationUserDto = new RegistrationUserDto();
		registrationUserDto.setName("Oleg");
		registrationUserDto.setPassword("password");
		registrationUserDto.setConfirmPassword("password");
		registrationUserDto.setEmail("Oleg@gmail.com");

		return registrationUserDto;
	}

	private UserDto createUserDto() {
		UserDto userDto = new UserDto();
		userDto.setName("Oleg");
		userDto.setEmail("userDto@gmail.com");

		return userDto;
	}
}
