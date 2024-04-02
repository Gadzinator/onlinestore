package com.onlinestore.main.service.impl;

import com.onlinestore.main.domain.dto.OrderDto;
import com.onlinestore.main.mapper.IOrderMapper;
import com.onlinestore.main.repository.IOrderRepository;
import com.onlinestore.main.repository.IProductRepository;
import com.onlinestore.main.service.IOrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class OrderService implements IOrderService {

	private IOrderRepository orderRepository;
	private IOrderMapper orderMapper;
	private IProductRepository productRepository;

	@Override
	public void add(OrderDto orderDto) {
		orderRepository.add(orderMapper.mapToOrderDto(orderDto));
	}

	@Override
	public OrderDto findById(long id) {
		return orderRepository.findById(id)
				.map(order -> orderMapper.mapToOrder(order))
				.orElseThrow(() -> new NoSuchElementException(
						String.format("Order with id %d was not found", id)));
	}

	@Override
	public void updateById(long id, OrderDto orderDtoUpdate) {
		orderRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException(
						String.format("Order with id %d was not found", id)));
		orderRepository.updateById(id, orderMapper.mapToOrderDto(orderDtoUpdate));
	}

	@Override
	public void deleteById(long id) {
		orderRepository.deleteById(id);
	}
}
