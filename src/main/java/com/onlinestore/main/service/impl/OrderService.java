package com.onlinestore.main.service.impl;

import com.onlinestore.main.domain.dto.OrderDto;
import com.onlinestore.main.domain.entity.Order;
import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.excepiton.OrderNotFoundException;
import com.onlinestore.main.mapper.IOrderMapper;
import com.onlinestore.main.repository.impl.OrderRepository;
import com.onlinestore.main.service.IOrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService implements IOrderService {

	private OrderRepository orderRepository;
	private IOrderMapper orderMapper;

	@Transactional
	@Override
	public void add(OrderDto orderDto) {
		if (orderDto == null) {
			throw new NullPointerException("ProductDto cannot be null");
		}

		orderRepository.add(orderMapper.mapToOrder(orderDto));
	}

	@Override
	public OrderDto findById(long id) {
		return orderRepository.findById(id).map(order -> orderMapper.mapToOrderDto(order)).orElseThrow(
				() -> new OrderNotFoundException("Order not was found with id " + id));
	}

	@Transactional
	@Override
	public List<OrderDto> findAll() {
		List<OrderDto> orderDtoList = new ArrayList<>();
		final List<Order> orderList = orderRepository.findAll();
		if (!orderList.isEmpty()) {
			for (Order order : orderList) {
				final OrderDto orderDto = orderMapper.mapToOrderDto(order);
				orderDtoList.add(orderDto);
			}
		} else {
			throw new OrderNotFoundException("Orders were not found");
		}

		return orderDtoList;
	}

	@Transactional
	@Override
	public void updateById(OrderDto orderDtoUpdate) {
		final OrderDto orderDto = findById(orderDtoUpdate.getId());
		if (orderDto != null) {
			final Order order = orderMapper.mapToOrder(orderDto);
			orderRepository.update(order);
		} else {
			throw new OrderNotFoundException("Order not was found with id " + orderDtoUpdate.getId());
		}
	}

	@Transactional
	@Override
	public void deleteById(long id) {
		final OrderDto orderDto = findById(id);
		if (orderDto != null) {
			orderRepository.deleteById(orderDto.getId());
		} else {
			throw new OrderNotFoundException("Order not was found with id " + id);
		}
	}

	@Override
	public List<Product> findProductsOrderId(long id) {
		final OrderDto orderDto = findById(id);
		if (orderDto != null) {
			return orderRepository.findProductsOrderId(orderDto.getId());
		} else {
			throw new OrderNotFoundException("Order not was found with id " + id);
		}
	}
}
