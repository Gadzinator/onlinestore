package com.onlinestore.main.domain.dto;

import lombok.Data;

@Data
public class JwtRequest {

	private String userName;

	private String password;
}
