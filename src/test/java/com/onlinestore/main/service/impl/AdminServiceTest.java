package com.onlinestore.main.service.impl;

import com.onlinestore.main.domain.entity.Role;
import com.onlinestore.main.domain.entity.User;
import com.onlinestore.main.repository.impl.UserRepository;
import com.onlinestore.main.service.impl.config.ServiceTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static com.onlinestore.main.domain.entity.Role.ROLE_USER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
public class AdminServiceTest {

	private static final String USER_NAME = "Alex";

	private static final String USER_EMAIL = "alex@gmail.ru";

	private static final String PASSWORD = "Alex";

	private static final long USER_ID = 1;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private AdminService adminService;

	@Test
	public void testChangeUserRoleUserNotFound() {
		String userName = "nonExistingUser";
		String newRole = "ROLE_ADMIN";

		when(userRepository.findByName(userName)).thenReturn(Optional.empty());

		assertThrows(UsernameNotFoundException.class, () -> adminService.changeUserRole(userName, newRole));

		verify(userRepository).findByName(userName);
		verifyNoMoreInteractions(userRepository);
	}

	@Test
	public void testChangeUserRoleValidRole() {
		String userName = "existingUser";
		String newRole = "ROLE_ADMIN";
		User existingUser = createUser();

		when(userRepository.findByName(userName)).thenReturn(Optional.of(existingUser));

		adminService.changeUserRole(userName, newRole);

		assertEquals(Role.valueOf(newRole), existingUser.getRole());

		verify(userRepository).findByName(userName);
		verify(userRepository).add(existingUser);
		verifyNoMoreInteractions(userRepository);
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
}
