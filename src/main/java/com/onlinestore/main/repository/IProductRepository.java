package com.onlinestore.main.repository;

import com.onlinestore.main.domain.entity.Product;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IProductRepository {

	Optional<Product> findByName(String name);

	List<Product> findByParams(Map<String, String> params);
}
