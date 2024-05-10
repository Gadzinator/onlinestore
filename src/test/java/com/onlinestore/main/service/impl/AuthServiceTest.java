package com.onlinestore.main.service.impl;

import com.onlinestore.main.domain.dto.JwtRequest;
import com.onlinestore.main.domain.dto.MyUserPrincipal;
import com.onlinestore.main.domain.entity.User;
import com.onlinestore.main.excepiton.AuthenticationFailedException;
import com.onlinestore.main.excepiton.UserNotFoundException;
import com.onlinestore.main.repository.impl.UserRepository;
import com.onlinestore.main.service.impl.config.ServiceTestConfiguration;
import com.onlinestore.main.utils.JwtTokenUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static com.onlinestore.main.domain.entity.Role.ROLE_USER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
public class AuthServiceTest {

	private static final String USER_NAME = "Alex";

	private static final String USER_EMAIL = "alex@gmail.ru";

	private static final String PASSWORD = "Alex";

	private static final long USER_ID = 1;

	private static final String TOKEN = "tokenString";

	@Mock
	private UserRepository userRepository;

	@Mock
	private JwtTokenUtils jwtTokenUtils;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private AuthService authService;

	@Test
	public void testCreateAuthTokenInvalidUsername() {
		JwtRequest authRequest = createJwtRequest();

		when(userRepository.findByName(USER_NAME)).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> authService.createAuthToken(authRequest));
	}

	@Test
	public void testCreateAuthTokenInvalidPassword() {
		JwtRequest authRequest = createJwtRequest();

		User user = createUser();
		UserDetails userDetails = new MyUserPrincipal(user);

		when(userRepository.findByName(USER_NAME)).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword())).thenReturn(false);

		assertThrows(AuthenticationFailedException.class, () -> authService.createAuthToken(authRequest));

		verify(userRepository).findByName(USER_NAME);
		verify(passwordEncoder).matches(authRequest.getPassword(), userDetails.getPassword());
	}

	@Test
	public void testCreateAuthTokenValidCredentials() {
		JwtRequest authRequest = createJwtRequest();

		User user = createUser();
		UserDetails userDetails = new MyUserPrincipal(user);

		when(userRepository.findByName(USER_NAME)).thenReturn(Optional.of(user));
		when(jwtTokenUtils.generateToken(userDetails)).thenReturn(TOKEN);
		when(passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword())).thenReturn(true);

		String token = authService.createAuthToken(authRequest);

		verify(userRepository).findByName(USER_NAME);
		verify(jwtTokenUtils).generateToken(userDetails);
		verify(passwordEncoder).matches(authRequest.getPassword(), userDetails.getPassword());
		assertEquals(TOKEN, token);
	}

	@Test
	public void loadUserByUsernameWhenUserNotExist() {
		when(userRepository.findByName(USER_NAME)).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> authService.loadUserByUsername(USER_NAME));

		verify(userRepository).findByName(USER_NAME);
	}

	@Test
	public void loadUserByUsernameWhenUserExist() {
		User user = createUser();

		when(userRepository.findByName(USER_NAME)).thenReturn(Optional.of(user));

		UserDetails userDetails = authService.loadUserByUsername(USER_NAME);

		verify(userRepository).findByName(USER_NAME);

		assertEquals(userDetails.getUsername(), user.getName());
		assertEquals(userDetails.getPassword(), user.getPassword());
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

	private JwtRequest createJwtRequest() {
		JwtRequest authRequest = new JwtRequest();
		authRequest.setUserName(USER_NAME);
		authRequest.setPassword(PASSWORD);

		return authRequest;
	}
}
