package com.onlinestore.main.repository.impl;

import com.onlinestore.main.domain.entity.Role;
import com.onlinestore.main.domain.entity.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository implements com.onlinestore.main.repository.IUserRepository {

	private final List<User> users = new ArrayList<>();

	@Override
	public void add(User user) {
		user.setRole(Role.ROLE_USER);
		users.add(user);
	}

	@Override
	public Optional<User> findById(long id) {
		return users.stream()
				.filter(user -> user.getId() == id)
				.findFirst();
	}

	@Override
	public void update(long id, User userUpdate) {
		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			if (user.getId() == id) {
				users.set(i, userUpdate);
			}
		}
	}

	@Override
	public void delete(long id) {
		users.removeIf(user -> user.getId() == id);
	}
}
