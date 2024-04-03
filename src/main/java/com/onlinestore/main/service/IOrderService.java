package com.onlinestore.main.service;

import com.onlinestore.main.domain.dto.OrderDto;

public interface IOrderService {
	void add(OrderDto orderDto);

	OrderDto findById(long id);

	void updateById(long id, OrderDto orderDtoUpdate);

	void deleteById(long id);
}
