package com.onlinestore.main.controller;

import com.onlinestore.main.controller.utils.JsonUtils;
import com.onlinestore.main.domain.dto.OrderDto;
import com.onlinestore.main.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class OrderController {

	private final IOrderService orderService;
	private final JsonUtils jsonUtils;

	public void add(OrderDto orderDto) {
		orderService.add(orderDto);

		String json = jsonUtils.getJson(orderDto);
		System.out.println("Method to add to OrderController - " + json);
	}

	public OrderDto findById(long id) {
		OrderDto orderDto = orderService.findById(id);

		String json = jsonUtils.getJson(orderDto);
		System.out.println("Method to findById to OrderController - " + json);

		return orderDto;
	}

	public void updateById(long id, OrderDto orderDtoUpdate) {
		orderService.updateById(id, orderDtoUpdate);

		String json = jsonUtils.getJson(orderDtoUpdate);
		System.out.println("Method to updateById to OrderController - " + json);
	}

	public void deleteById(long id) {
		orderService.deleteById(id);

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Order with Id " + id + " has been successfully deleted");
		String json = jsonUtils.getJson(response);
		System.out.println(json);
	}
}
