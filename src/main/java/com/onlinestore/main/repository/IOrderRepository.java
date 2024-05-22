package com.onlinestore.main.repository;

import com.onlinestore.main.domain.entity.Order;

import java.util.List;

public interface IOrderRepository {

	List<Order> findOrdersByProductId(long productId);
}
