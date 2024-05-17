package com.onlinestore.main.service;

import com.onlinestore.main.domain.dto.OrderRequestDto;
import com.onlinestore.main.domain.dto.OrderResponseDto;
import com.onlinestore.main.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {

	void save(OrderRequestDto orderRequestDto);

	OrderResponseDto findById(long id);

	Page<OrderResponseDto> findAll(Pageable pageable);

	void update(OrderRequestDto orderRequestDto);

	List<Product> findProductsByOrderId(long id);

	void deleteById(long id);
}
