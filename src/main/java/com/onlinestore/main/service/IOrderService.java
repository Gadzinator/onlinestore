package com.onlinestore.main.service;

import com.onlinestore.main.domain.dto.OrderDto;
import com.onlinestore.main.domain.entity.Product;

import java.util.List;

public interface IOrderService {
	void add(OrderDto orderDto);

	OrderDto findById(long id);

	public List<OrderDto> findAll();

	void updateById(OrderDto orderDtoUpdate);

	List<Product> findProductsOrderId(long id);

	void deleteById(long id);
}
