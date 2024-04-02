package com.onlinestore.main.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Data
@Component
public class Order {

	private long id;

	private LocalDate created;

	private List<Product> products;

	private OrderStatus orderStatus;
}
