package com.onlinestore.main.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class RegistrationUserDto {

	private long id;

	private String name;

	private String password;

	@JsonProperty("confirm_password")
	private String confirmPassword;

	private String email;
}
