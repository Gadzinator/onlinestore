package com.onlinestore.main.repository;

import com.onlinestore.main.domain.entity.Product;

import java.util.Optional;

public interface IProductRepository {
    void add(Product product);

    Optional<Product> findById(long id);

    void updateById(long id, Product updateProduct);

    void delete(long id);
}
