package com.onlinestore.main.exception;

public class ProductInUseException extends RuntimeException {

	public ProductInUseException(String message) {
		super(message);
	}
}
