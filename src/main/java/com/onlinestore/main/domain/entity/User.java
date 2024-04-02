package com.onlinestore.main.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class User {

	private long id;

	private String name;

	private String email;

	private String password;

	private Role role;
}
