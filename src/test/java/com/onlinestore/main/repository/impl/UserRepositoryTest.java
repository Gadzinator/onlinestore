package com.onlinestore.main.repository.impl;

import com.onlinestore.main.domain.entity.Role;
import com.onlinestore.main.domain.entity.User;
import com.onlinestore.main.repository.impl.config.H2Config;
import com.onlinestore.main.repository.impl.config.LiquibaseConfigTest;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {H2Config.class, LiquibaseConfigTest.class})
@Transactional
public class UserRepositoryTest {

	@Resource
	private UserRepository userRepositoryDao;

	private final String USER_NAME = "Alex";

	private static final String UPDATE_USER_NAME = "updated_username";

	@Test
	public void testAdd() {
		User user = createUser();

		userRepositoryDao.add(user);

		final Optional<User> optionalUser = userRepositoryDao.findById(user.getId());

		assertTrue(optionalUser.isPresent());
		assertEquals(user.getName(), optionalUser.get().getName());
		assertEquals(user.getEmail(), optionalUser.get().getEmail());
	}

	@Test
	public void testFindAll() {
		User user = createUser();
		User user1 = createUser();
		userRepositoryDao.add(user);
		userRepositoryDao.add(user1);

		List<User> userList = userRepositoryDao.findAll();

		assertFalse(userList.isEmpty());
	}

	@Test
	public void testUpdate() {
		User user = createUser();

		userRepositoryDao.add(user);

		user.setName(UPDATE_USER_NAME);

		userRepositoryDao.update(user);

		User updatedUser = userRepositoryDao.findById(user.getId()).orElse(null);

		assertEquals(UPDATE_USER_NAME, Objects.requireNonNull(updatedUser).getName());
	}

	@Test
	public void testDeleteById() {
		User user = createUser();

		userRepositoryDao.add(user);

		userRepositoryDao.deleteById(user.getId());

		Optional<User> deletedUser = userRepositoryDao.findById(user.getId());
		assertTrue(deletedUser.isEmpty());
	}

	@Test
	public void testFindById() {
		User user = createUser();

		userRepositoryDao.add(user);

		Optional<User> optionalUser = userRepositoryDao.findById(user.getId());

		assertTrue(optionalUser.isPresent());
		assertEquals(Optional.of(user.getId()), optionalUser.map(User::getId));
	}

	@Test
	public void findByName() {
		User user = createUser();

		userRepositoryDao.add(user);

		Optional<User> optionalUser = userRepositoryDao.findByName(USER_NAME);

		assertTrue(optionalUser.isPresent());
	}

	private User createUser() {
		User user = new User();
		user.setName(USER_NAME);
		user.setRole(Role.ROLE_USER);
		user.setPassword("password");
		user.setEmail("@email");

		return user;
	}
}