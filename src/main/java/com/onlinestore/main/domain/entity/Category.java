package com.onlinestore.main.domain.entity;

public enum Category {

	TOY("TOY"), CLOTHES("CLOTHES"), ELECTRONICS("ELECTRONICS");

	private final String value;

	Category(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
