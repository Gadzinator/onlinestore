package com.onlinestore.main.repository;

import com.onlinestore.main.domain.entity.Order;

import java.util.Optional;

public interface IOrderRepository {
    void add(Order order);

    Optional<Order> findById(long id);

    void updateById(long id, Order updateOrder);

    void deleteById(long id);
}
