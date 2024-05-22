package com.onlinestore.main.service;

import com.onlinestore.main.domain.dto.OrderRequestDto;
import com.onlinestore.main.domain.dto.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderService {

	void save(OrderRequestDto orderRequestDto);

	OrderResponseDto findById(long id);

	Page<OrderResponseDto> findAll(Pageable pageable);

	void update(OrderRequestDto orderRequestDto);

	void deleteById(long id);
}
