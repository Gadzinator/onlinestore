package com.onlinestore.main.service.impl;

import com.onlinestore.main.domain.dto.RegistrationUserDto;
import com.onlinestore.main.domain.dto.UserDto;
import com.onlinestore.main.domain.entity.Role;
import com.onlinestore.main.domain.entity.User;
import com.onlinestore.main.excepiton.UserNotFoundException;
import com.onlinestore.main.mapper.IUserMapperImpl;
import com.onlinestore.main.repository.impl.UserRepository;
import com.onlinestore.main.service.impl.config.ServiceTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
public class UserServiceTest {

	private static final String USER_NAME = "Alex";

	private static final long USER_ID = 1;

	@Mock
	private UserRepository userRepository;

	@Mock
	private IUserMapperImpl userMapper;

	@InjectMocks
	private UserService userService;

	@Test
	public void testCreateNewUser() {
		final RegistrationUserDto registrationUserDto = createRegistrationUserDto();

		when(userMapper.mapToUserDto(any(User.class))).thenCallRealMethod();

		final UserDto createdUserDto = userService.createNewUser(registrationUserDto);

		final ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
		verify(userRepository).add(userArgumentCaptor.capture());
		final User savedUser = userArgumentCaptor.getValue();
		verify(userMapper).mapToUserDto(savedUser);

		assertEquals(savedUser.getId(), createdUserDto.getId());
		assertEquals(savedUser.getName(), createdUserDto.getName());
		assertEquals(savedUser.getEmail(), createdUserDto.getEmail());
	}

	@Test
	public void testCreateNewUserWithUserNull() {
		assertThrows(NullPointerException.class, () -> userService.createNewUser(null));
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
		registrationUserDto.setName("Alex");
		registrationUserDto.setPassword("password");
		registrationUserDto.setEmail("alex@gmail.com");

		return registrationUserDto;
	}

	private User createUser() {
		User user = new User();
		user.setId(USER_ID);
		user.setName(USER_NAME);
		user.setRole(Role.ROLE_USER);
		user.setPassword("password");
		user.setEmail("@email");

		return user;
	}

	private UserDto createUserDto() {
		UserDto userDto = new UserDto();
		userDto.setId(USER_ID);
		userDto.setName("User Dto");
		userDto.setEmail("userDto@gmail.com");

		return userDto;
	}
}
