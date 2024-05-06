package com.onlinestore.main.domain.dto;

import lombok.Data;

@Data
public class UserRoleChangeRequest {

	private String userName;

	private String newRole;
}
