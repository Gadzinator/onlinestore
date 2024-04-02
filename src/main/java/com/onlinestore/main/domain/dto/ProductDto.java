package com.onlinestore.main.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ProductDto {

	private long id;

	private String name;

	private String brand;

	private String description;

	private String category;

	private int price;

	@JsonFormat(pattern = "dd-MM-yyyy")
	private String created;

	@JsonProperty("is_available")
	private boolean isAvailable;

	@JsonFormat(pattern = "dd-MM-yyyy")
	private String received;
}
