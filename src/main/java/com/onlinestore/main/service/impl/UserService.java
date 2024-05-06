package com.onlinestore.main.service.impl;

import com.onlinestore.main.domain.dto.PasswordChangeRequest;
import com.onlinestore.main.domain.dto.RegistrationUserDto;
import com.onlinestore.main.domain.dto.UserDto;
import com.onlinestore.main.domain.entity.Role;
import com.onlinestore.main.domain.entity.User;
import com.onlinestore.main.excepiton.PasswordMismatchException;
import com.onlinestore.main.excepiton.UserNotFoundException;
import com.onlinestore.main.excepiton.UsernameNotUniqueException;
import com.onlinestore.main.mapper.IUserMapper;
import com.onlinestore.main.repository.impl.UserRepository;
import com.onlinestore.main.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

	private UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private IUserMapper userMapper;

	@Transactional
	@Override
	public UserDto createNewUser(RegistrationUserDto registrationUserDto) {
		validateUsernameIsUnique(registrationUserDto.getName());
		validatePasswordMatch(registrationUserDto.getPassword(), registrationUserDto.getConfirmPassword());
		validateEmailIsUnique(registrationUserDto.getEmail());

		User user = createUserFromRegistrationUserDto(registrationUserDto);
		userRepository.add(user);

		return userMapper.mapToUserDto(user);
	}

	@Override
	public UserDto findById(long id) {
		return userRepository.findById(id)
				.map(user -> userMapper.mapToUserDto(user))
				.orElseThrow(() -> new UserNotFoundException("User was not found with id " + id));
	}

	@Override
	public UserDto findByName(String name) {
		return userRepository.findByName(name)
				.map(user -> userMapper.mapToUserDto(user))
				.orElseThrow(() -> new UserNotFoundException("User was not found with name " + name));
	}

	@Override
	public List<UserDto> findAll() {
		List<UserDto> userDtoList = new ArrayList<>();
		final List<User> userList = userRepository.findAll();
		if (!userList.isEmpty()) {
			for (User user : userList) {
				final UserDto userDto = userMapper.mapToUserDto(user);
				userDtoList.add(userDto);
			}
		} else {
			throw new UserNotFoundException("Users were not found");
		}

		return userDtoList;
	}

	@Transactional
	public void deleteById(long id) {
		UserDto userDto = findById(id);
		if (userDto != null) {
			userRepository.deleteById(id);
		}
	}

	@Override
	@Transactional
	public void changePassword(String userName, PasswordChangeRequest passwordChangeRequest) {
		String enteredPassword = passwordChangeRequest.getOldPassword();
		final User user = userRepository.findByName(userName)
				.orElseThrow(() -> new UserNotFoundException("User was not found with name " + userName));

		String storedPassword = user.getPassword();

		if (!passwordEncoder.matches(enteredPassword, storedPassword)) {
			throw new PasswordMismatchException("Passwords do not match");
		}
		user.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
		userRepository.add(user);
	}

	private void validatePasswordMatch(String password, String confirmPassword) {
		if (!password.equals(confirmPassword)) {
			throw new PasswordMismatchException("Passwords do not match");
		}
	}

	private void validateUsernameIsUnique(String name) {
		final Optional<User> optionalUser = userRepository.findByName(name);
		if (optionalUser.isPresent()) {
			throw new UsernameNotUniqueException("User with name " + name + " already exists");
		}
	}

	private void validateEmailIsUnique(String email) {
		final Optional<User> optionalUser = userRepository.findUserByEmail(email);
		if (optionalUser.isPresent()) {
			throw new UsernameNotUniqueException("User with name " + email + " already exists");
		}
	}

	private User createUserFromRegistrationUserDto(RegistrationUserDto registrationUserDto) {
		User user = new User();
		user.setName(registrationUserDto.getName());
		user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
		user.setEmail(registrationUserDto.getEmail());
		user.setRole(Role.ROLE_USER);

		return user;
	}
}
