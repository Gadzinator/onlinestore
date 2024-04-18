package com.onlinestore.main.service.impl;

import com.onlinestore.main.domain.dto.OrderDto;
import com.onlinestore.main.domain.entity.Order;
import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.mapper.IOrderMapper;
import com.onlinestore.main.repository.impl.OrderRepositoryDao;
import com.onlinestore.main.service.IOrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class OrderService implements IOrderService {

	private OrderRepositoryDao orderRepository;
	private IOrderMapper orderMapper;

	@Transactional
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

	@Transactional
	@Override
	public List<OrderDto> findAll() {
		List<OrderDto> orderDtoList = new ArrayList<>();
		for (Order order : orderRepository.findAll()) {
			final OrderDto orderDto = orderMapper.mapToOrder(order);
			orderDtoList.add(orderDto);
		}

		return orderDtoList;
	}

	@Transactional
	@Override
	public void updateById(OrderDto orderDtoUpdate) {
		final OrderDto orderDto = findById(orderDtoUpdate.getId());
		orderRepository.update(orderMapper.mapToOrderDto(orderDto));
	}

	@Transactional
	@Override
	public void deleteById(long id) {
		final OrderDto orderDto = findById(id);
		orderRepository.deleteById(orderDto.getId());
	}

	@Override
	public List<Product> findProductsOrderId(long id) {
		final OrderDto orderDto = findById(id);

		return orderRepository.findProductsOrderId(orderDto.getId());
	}
}
