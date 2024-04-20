package com.onlinestore.main.controller;

import com.onlinestore.main.controller.utils.JsonUtils;
import com.onlinestore.main.domain.dto.RegistrationUserDto;
import com.onlinestore.main.domain.dto.UserDto;
import com.onlinestore.main.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class UserController {

	private final IUserService userService;
	private final JsonUtils jsonUtils;

	public UserDto createNewUser(RegistrationUserDto registrationUserDto) {
		UserDto newUser = userService.createNewUser(registrationUserDto);

		String json = jsonUtils.getJson(newUser);
		System.out.println("Method to createNewUser to UserController - " + json);

		return newUser;
	}

	public UserDto findById(long id) {
		UserDto userDto = userService.findById(id);

		String json = jsonUtils.getJson(userDto);
		System.out.println("Method to findById to UserController - " + json);

		return userDto;
	}

	public List<UserDto> findAll() {
		final List<UserDto> userDtoList = userService.findAll();
		for (UserDto userDto : userDtoList) {
			final String json = jsonUtils.getJson(userDto);
			System.out.println("Method to findAll to UserController - " + json);
		}

		return userDtoList;
	}

	public UserDto findByName(String name) {
		final UserDto userDto = userService.findByName(name);

		String json = jsonUtils.getJson(userDto);
		System.out.println("Method to findByName to UserController - " + json);

		return userDto;
	}

	public void deleteById(long id) {
		userService.deleteById(id);

		Map<String, Object> response = new HashMap<>();
		response.put("Message", "User with Id " + id + " has been successfully deleted");
		String json = jsonUtils.getJson(response);
		System.out.println(json);
	}
}
