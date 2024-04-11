package com.onlinestore.main.execute;

import com.onlinestore.main.controller.OrderController;
import com.onlinestore.main.domain.dto.OrderDto;

import java.util.function.Consumer;

public class MyRunnable implements Runnable {
	private final String threadName;
	private final Consumer<OrderDto> orderMethod;
	private final OrderDto orderDto;

	public MyRunnable(String threadName, Consumer<OrderDto> orderMethod, OrderDto orderDto) {
		this.threadName = threadName;
		this.orderMethod = orderMethod;
		this.orderDto = orderDto;
	}

	@Override
	public void run() {
		orderMethod.accept(orderDto);
		System.out.println("Thread " + threadName + " is running.");
	}
}
