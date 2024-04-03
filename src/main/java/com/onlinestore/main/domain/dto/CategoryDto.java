package com.onlinestore.main.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CategoryDto {

	@JsonProperty("id")
	private long id;

	@JsonProperty("name")
	private String name;
}
