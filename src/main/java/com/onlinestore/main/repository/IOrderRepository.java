package com.onlinestore.main.repository;

import com.onlinestore.main.domain.entity.Order;
import com.onlinestore.main.domain.entity.Product;

import java.util.List;

public interface IOrderRepository {

	List<Product> findProductsOrderId(long id);

	List<Order> findOrdersByProductId(long productId);
}
