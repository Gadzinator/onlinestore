package com.onlinestore.main.service;

import com.onlinestore.main.domain.dto.PasswordChangeRequest;
import com.onlinestore.main.domain.dto.RegistrationUserDto;
import com.onlinestore.main.domain.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {

	UserDto createNewUser(RegistrationUserDto registrationUserDto);

	UserDto findById(long id);

	Page<UserDto> findAll(Pageable pageable);

	UserDto findByName(String name);

	void deleteById(long id);

	void changePassword(String userName, PasswordChangeRequest passwordChangeRequest);
}
