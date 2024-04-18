package com.onlinestore.main.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@NamedEntityGraph(name = "graph.User.role",
attributeNodes = @NamedAttributeNode(value = "role"))
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "name")
	private String name;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Role role;
}
