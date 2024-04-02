package com.onlinestore.main.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class WaitingLIstDto {

	private long id;

	@JsonProperty("products")
	private ProductDto productDto;

	private int counter;
}
