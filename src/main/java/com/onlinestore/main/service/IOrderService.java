package com.onlinestore.main.service;

import com.onlinestore.main.domain.dto.OrderDto;
import com.onlinestore.main.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {
	void add(OrderDto orderDto);

	OrderDto findById(long id);

	Page<OrderDto> findAll(Pageable pageable);

	void update(OrderDto orderDtoUpdate);

	List<Product> findProductsByOrderId(long id);

	void deleteById(long id);
}
