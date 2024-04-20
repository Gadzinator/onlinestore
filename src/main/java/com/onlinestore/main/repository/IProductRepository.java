package com.onlinestore.main.repository;

import com.onlinestore.main.domain.entity.Product;

import java.util.Optional;

public interface IProductRepository {

	Optional<Product> findByName(String name);
}
