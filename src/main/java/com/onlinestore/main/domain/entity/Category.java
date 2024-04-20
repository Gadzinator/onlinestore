package com.onlinestore.main.domain.entity;

import lombok.Getter;

@Getter
public enum Category {

	TOY("TOY"), CLOTHES("CLOTHES"), ELECTRONICS("ELECTRONICS");

	private final String value;

	Category(String value) {
		this.value = value;
	}
}
