package com.onlinestore.main.repository.impl;

import com.onlinestore.main.domain.entity.Order;
import com.onlinestore.main.repository.IOrderRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository implements IOrderRepository {

	private final List<Order> orders = new ArrayList<>();

	@Override
	public void add(Order order) {
		orders.add(order);
	}

	@Override
	public Optional<Order> findById(long id) {
		return orders.stream()
				.filter(order -> order.getId() == id)
				.findFirst();
	}

	@Override
	public void updateById(long id, Order updateOrder) {
		for (int i = 0; i < orders.size(); i++) {
			Order order = orders.get(i);
			if (order.getId() == id) {
				orders.set(i, updateOrder);
			}
		}
	}

	@Override
	public void deleteById(long id) {
		orders.removeIf(order -> order.getId() == id);
	}
}
