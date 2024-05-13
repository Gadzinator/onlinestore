package com.onlinestore.main.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto {

	private long id;

	@JsonFormat(pattern = "dd-MM-yyyy")
	@NotBlank(message = "created should not be blank")
	private String created;

	@Valid
	@NotNull(message = "products should not be null")
	@Size(min = 1, message = "at least one product should be present")
	@JsonProperty("products")
	private List<ProductDto> products;

	@JsonProperty("order_status")
	@NotBlank(message = "orderStatus should not be blank")
	private String orderStatus;
}
