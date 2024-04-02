package com.onlinestore.main.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Data
@Component
public class Product {

	private long id;

	private String name;

	private String brand;

	private String description;

	private Category category;

	private int price;

	private LocalDate created;

	private boolean isAvailable;

	private LocalDate received;
}
