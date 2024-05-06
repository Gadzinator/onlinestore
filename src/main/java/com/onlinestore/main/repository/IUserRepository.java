package com.onlinestore.main.repository;

import com.onlinestore.main.domain.entity.User;

import java.util.Optional;

public interface IUserRepository {

	Optional<User> findByName(String name);

	Optional<User> findUserByEmail(String email);
}
