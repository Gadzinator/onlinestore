package com.onlinestore.main.repository;

import com.onlinestore.main.domain.entity.Category;

import java.util.Optional;

public interface ICategoryRepository {

	Optional<Category> findByName(String name);
}
