package com.onlinestore.main.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegistrationUserDto {

	private long id;

	private String name;

	private String password;

	@JsonProperty("confirm_password")
	private String confirmPassword;

	private String email;
}
