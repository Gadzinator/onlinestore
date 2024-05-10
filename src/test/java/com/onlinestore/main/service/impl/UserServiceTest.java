package com.onlinestore.main.service.impl;

import com.onlinestore.main.domain.dto.RegistrationUserDto;
import com.onlinestore.main.domain.dto.UserDto;
import com.onlinestore.main.domain.entity.User;
import com.onlinestore.main.excepiton.PasswordMismatchException;
import com.onlinestore.main.excepiton.UserNotFoundException;
import com.onlinestore.main.excepiton.UsernameNotUniqueException;
import com.onlinestore.main.mapper.IUserMapperImpl;
import com.onlinestore.main.repository.impl.UserRepository;
import com.onlinestore.main.service.impl.config.ServiceTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.onlinestore.main.domain.entity.Role.ROLE_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
public class UserServiceTest {

	private static final String USER_NAME = "Alex";

	private static final String USER_EMAIL = "alex@gmail.ru";

	private static final String PASSWORD = "Alex";

	private static final long USER_ID = 1;

	@Mock
	private UserRepository userRepository;

	@Mock
	private IUserMapperImpl userMapper;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Captor
	private ArgumentCaptor<User> userArgumentCaptor;

	@InjectMocks
	private UserService userService;

	@Test
	public void testCreateNewUserWhenUserValidate() {
		RegistrationUserDto registrationUserDto = createRegistrationUserDto();
		UserDto userDto = createUserDto();

		when(userRepository.findByName(USER_NAME)).thenReturn(Optional.empty());
		when(userRepository.findUserByEmail(USER_EMAIL)).thenReturn(Optional.empty());
		when(passwordEncoder.encode(anyString())).thenReturn(PASSWORD);

		doNothing().when(userRepository).add(any(User.class));
		when(userMapper.mapToUserDto(any(User.class))).thenCallRealMethod();

		userService.createNewUser(registrationUserDto);

		verify(userRepository).findByName(USER_NAME);
		verify(userRepository).findUserByEmail(USER_EMAIL);
		verify(passwordEncoder).encode(PASSWORD);
		verify(userRepository).add(userArgumentCaptor.capture());
		verify(userMapper).mapToUserDto(userArgumentCaptor.capture());

		User savedUser = userArgumentCaptor.getValue();
		assertEquals(savedUser.getName(), userDto.getName());
		assertEquals(savedUser.getEmail(), userDto.getEmail());
	}

	@Test
	public void testCreateNewUserWhenPasswordNotValidate() {
		RegistrationUserDto registrationUserDto = createRegistrationUserDto();
		registrationUserDto.setConfirmPassword("sdasdasdas");

		when(userRepository.findByName(USER_NAME)).thenReturn(Optional.empty());

		assertThrows(PasswordMismatchException.class, () -> userService.createNewUser(registrationUserDto));

		verify(userRepository).findByName(USER_NAME);
	}

	@Test
	public void testCreateNewUserWhenEmailNotValidate() {
		RegistrationUserDto registrationUserDto = createRegistrationUserDto();

		when(userRepository.findByName(USER_NAME)).thenReturn(Optional.empty());
		when(userRepository.findUserByEmail(USER_EMAIL)).thenReturn(Optional.of(new User()));

		assertThrows(UsernameNotUniqueException.class, () -> userService.createNewUser(registrationUserDto));

		verify(userRepository).findByName(USER_NAME);
		verify(userRepository).findUserByEmail(USER_EMAIL);
	}

	@Test
	public void testFindByIdWhenUserExist() {
		final User user = createUser();
		final UserDto expectedUserDto = createUserDto();

		when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
		when(userMapper.mapToUserDto(user)).thenReturn(expectedUserDto);

		final UserDto actualUserDto = userService.findById(USER_ID);

		verify(userRepository).findById(USER_ID);
		verify(userMapper).mapToUserDto(user);

		assertEquals(expectedUserDto.getId(), actualUserDto.getId());
		assertEquals(expectedUserDto.getName(), actualUserDto.getName());
		assertEquals(expectedUserDto.getEmail(), actualUserDto.getEmail());
	}

	@Test
	public void testFindByIdWhenUserNotExist() {
		when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> userService.findById(USER_ID));
	}

	@Test
	public void testFindByNameUserExist() {
		final User user = createUser();
		final UserDto expectedUserDto = createUserDto();

		when(userRepository.findByName(USER_NAME)).thenReturn(Optional.of(user));
		when(userMapper.mapToUserDto(user)).thenReturn(expectedUserDto);

		final UserDto actualUserDto = userService.findByName(USER_NAME);

		verify(userRepository).findByName(USER_NAME);
		verify(userMapper).mapToUserDto(user);

		assertEquals(expectedUserDto.getId(), actualUserDto.getId());
		assertEquals(expectedUserDto.getName(), actualUserDto.getName());
		assertEquals(expectedUserDto.getEmail(), actualUserDto.getEmail());
	}

	@Test
	public void testFindByNameUserNotExist() {
		when(userRepository.findByName(USER_NAME)).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> userService.findByName(USER_NAME));
	}

	@Test
	public void testFindAllWhenListNotEmpty() {
		final User firstUser = createUser();
		final User secondUser = createUser();
		secondUser.setId(2);
		final List<User> users = Arrays.asList(firstUser, secondUser);

		final UserDto firstUserDto = createUserDto();
		final UserDto secondUserDto = createUserDto();
		secondUserDto.setId(2);

		when(userRepository.findAll()).thenReturn(users);
		when(userMapper.mapToUserDto(firstUser)).thenReturn(firstUserDto);
		when(userMapper.mapToUserDto(secondUser)).thenReturn(secondUserDto);

		final List<UserDto> actualeUserDtoList = userService.findAll();

		verify(userRepository).findAll();
		verify(userMapper).mapToUserDto(firstUser);
		verify(userMapper).mapToUserDto(secondUser);

		assertFalse(actualeUserDtoList.isEmpty());
		assertEquals(2, actualeUserDtoList.size());
		assertTrue(actualeUserDtoList.contains(firstUserDto));
		assertTrue(actualeUserDtoList.contains(secondUserDto));
	}

	@Test
	public void testFindAllWhenNotUsersExist() {
		when(userRepository.findAll()).thenReturn(Collections.emptyList());

		assertThrows(UserNotFoundException.class, () -> userService.findAll());
	}

	@Test
	public void testDeleteByIdWhenUserExist() {
		final User user = createUser();
		final UserDto expectedUserDto = createUserDto();

		when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
		when(userMapper.mapToUserDto(user)).thenReturn(expectedUserDto);

		userService.deleteById(USER_ID);

		verify(userRepository).findById(USER_ID);
		verify(userMapper).mapToUserDto(user);
		verify(userRepository).deleteById(USER_ID);
	}

	@Test
	public void testDeleteByIdWhenUserNotExist() {
		when(userRepository.findById(USER_ID)).thenThrow(UserNotFoundException.class);

		assertThrows(UserNotFoundException.class, () -> userService.deleteById(USER_ID));
		verify(userRepository, never()).deleteById(USER_ID);
	}

	private RegistrationUserDto createRegistrationUserDto() {
		RegistrationUserDto registrationUserDto = new RegistrationUserDto();
		registrationUserDto.setId(USER_ID);
		registrationUserDto.setName(USER_NAME);
		registrationUserDto.setPassword(PASSWORD);
		registrationUserDto.setConfirmPassword(PASSWORD);
		registrationUserDto.setEmail(USER_EMAIL);

		return registrationUserDto;
	}

	private User createUser() {
		User user = new User();
		user.setId(USER_ID);
		user.setName(USER_NAME);
		user.setRole(ROLE_USER);
		user.setPassword(PASSWORD);
		user.setEmail(USER_EMAIL);

		return user;
	}

	private UserDto createUserDto() {
		UserDto userDto = new UserDto();
		userDto.setId(USER_ID);
		userDto.setName(USER_NAME);
		userDto.setEmail(USER_EMAIL);

		return userDto;
	}
}
