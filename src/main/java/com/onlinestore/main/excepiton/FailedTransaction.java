package com.onlinestore.main.excepiton;

public class FailedTransaction extends RuntimeException {

	public FailedTransaction(String message, Throwable cause) {
		super(message, cause);
	}
}
