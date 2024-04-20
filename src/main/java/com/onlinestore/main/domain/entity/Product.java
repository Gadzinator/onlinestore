package com.onlinestore.main.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "name")
	private String name;

	@Column(name = "brand")
	private String brand;

	@Column(name = "description")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "category")
	private Category category;

	@Column(name = "price")
	private int price;

	@Column(name = "created")
	private LocalDate created;

	@Column(name = "is_available")
	private boolean isAvailable;

	@Column(name = "received")
	private LocalDate received;
}
