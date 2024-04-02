package com.onlinestore.main.domain.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserDto {

	private long id;

	private String name;

	private String email;
}
