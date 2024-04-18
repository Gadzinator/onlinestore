package com.onlinestore.main.service;

import com.onlinestore.main.domain.dto.RegistrationUserDto;
import com.onlinestore.main.domain.dto.UserDto;
import com.onlinestore.main.domain.entity.Order;
import com.onlinestore.main.domain.entity.User;

import java.util.List;

public interface IUserService {
	UserDto createNewUser(RegistrationUserDto registrationUserDto);

	UserDto findById(long id);

	List<UserDto> findAll();

	UserDto findByName(String name);

	void deleteById(long id);
}
