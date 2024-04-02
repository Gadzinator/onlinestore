package com.onlinestore.main.service.impl;

import com.onlinestore.main.domain.dto.RegistrationUserDto;
import com.onlinestore.main.domain.dto.UserDto;
import com.onlinestore.main.domain.entity.Role;
import com.onlinestore.main.domain.entity.User;
import com.onlinestore.main.mapper.IUserMapper;
import com.onlinestore.main.repository.IUserRepository;
import com.onlinestore.main.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@AllArgsConstructor
public class UserService implements IUserService {

	private IUserRepository userRepository;
	private IUserMapper userMapper;

	@Override
	public UserDto createNewUser(RegistrationUserDto registrationUserDto) {
		User user = createUserFromRegistrationUserDto(registrationUserDto);
		userRepository.add(user);

		return userMapper.mapToUserDto(user);
	}

	@Override
	public UserDto findById(long id) {
		return userRepository.findById(id)
				.map(user -> userMapper.mapToUserDto(user))
				.orElseThrow(() -> new NoSuchElementException(
						String.format("User with id %d was not found", id)));
	}

	public void deleteById(long id) {
		UserDto userDto = findById(id);
		if (userDto != null) {
			userRepository.delete(id);
		}
	}

	private User createUserFromRegistrationUserDto(RegistrationUserDto registrationUserDto) {
		User user = new User();
		user.setId(registrationUserDto.getId());
		user.setName(registrationUserDto.getName());
		user.setPassword(registrationUserDto.getPassword());
		user.setEmail(registrationUserDto.getEmail());
		user.setRole(Role.ROLE_USER);

		return user;
	}
}
