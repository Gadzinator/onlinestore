package com.onlinestore.main.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.onlinestore.main.domain.entity.OrderStatus;
import com.onlinestore.main.utils.DateConstant;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {

	private long id;

	private long userId;

	@JsonFormat(pattern = DateConstant.DEFAULT_DATE_PATTERN)
	@NotBlank(message = "created should not be blank")
	private String created;

	@Valid
	@NotNull(message = "productIds should not be null")
	@Size(min = 1, message = "at least one productsId should be present")
	private List<Long> productIds;

	@JsonProperty("orderStatus")
	@NotBlank(message = "orderStatus should not be blank")
	private OrderStatus orderStatus;
}