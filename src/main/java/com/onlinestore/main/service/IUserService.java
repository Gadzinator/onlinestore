package com.onlinestore.main.service;

import com.onlinestore.main.domain.dto.PasswordChangeRequest;
import com.onlinestore.main.domain.dto.RegistrationUserDto;
import com.onlinestore.main.domain.dto.UserDto;

import java.util.List;

public interface IUserService {

	UserDto createNewUser(RegistrationUserDto registrationUserDto);

	UserDto findById(long id);

	List<UserDto> findAll();

	UserDto findByName(String name);

	void deleteById(long id);

	void changePassword(String userName, PasswordChangeRequest passwordChangeRequest);
}
