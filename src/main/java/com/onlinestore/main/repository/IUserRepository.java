package com.onlinestore.main.repository;

import com.onlinestore.main.domain.entity.User;

import java.util.Optional;

public interface IUserRepository {
	void add(User user);

	Optional<User> findById(long id);

	void update(long id, User userUpdate);

	void delete(long id);
}
