package com.onlinestore.main.repository;

import com.onlinestore.main.domain.entity.Product;

import java.util.List;

public interface IOrderRepository {

	List<Product> findProductsOrderId(long id);
}
