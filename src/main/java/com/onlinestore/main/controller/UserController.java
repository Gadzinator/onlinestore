package com.onlinestore.main.controller;

import com.onlinestore.main.controller.utils.JsonUtils;
import com.onlinestore.main.domain.dto.RegistrationUserDto;
import com.onlinestore.main.domain.dto.UserDto;
import com.onlinestore.main.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Controller
public class UserController {

	private IUserService userService;

	public UserDto createNewUser(RegistrationUserDto registrationUserDto) {
		UserDto newUser = userService.createNewUser(registrationUserDto);

		String json = JsonUtils.getJson(newUser);
		System.out.println("Method to createNewUser to UserController - " + json);

		return newUser;
	}

	public UserDto findById(long id) {
		UserDto userDto = userService.findById(id);

		String json = JsonUtils.getJson(userDto);
		System.out.println("Method to findById to UserController - " + json);

		return userDto;
	}

	public void deleteById(long id) {
		userService.deleteById(id);

		Map<String, Object> response = new HashMap<>();
		response.put("Message", "User with Id " + id + " has been successfully deleted");
		String json = JsonUtils.getJson(response);
		System.out.println(json);
	}
}
