package com.onlinestore.main.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception e) {
		log.error("Exception occurred ", e);
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		final String stackTrace = sw.toString();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(stackTrace);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException exception, WebRequest request) {
		log.error("User not found ", exception);

		return handleGlobal(exception, request, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<?> handleOrderNotFoundException(OrderNotFoundException exception, WebRequest request) {
		log.error("Order not found ", exception);

		return handleGlobal(exception, request, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<?> handleProductNotFoundException(ProductNotFoundException exception, WebRequest request) {
		log.error("Product not found ", exception);

		return handleGlobal(exception, request, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UsernameNotUniqueException.class)
	public ResponseEntity<?> handleUsernameNotUniqueException(UsernameNotUniqueException exception, WebRequest request) {
		log.error("Username not unique ", exception);

		return handleGlobal(exception, request, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(EmailNotUniqueException.class)
	public ResponseEntity<?> handleEmailNotUniqueException(EmailNotUniqueException exception, WebRequest request) {
		log.error("Email not unique ", exception);

		return handleGlobal(exception, request, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(PasswordMismatchException.class)
	public ResponseEntity<?> handlePasswordMismatchException(PasswordMismatchException exception, WebRequest request) {
		log.error("Password mismatch ", exception);

		return handleGlobal(exception, request, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(WaitingListNotFoundException.class)
	public ResponseEntity<?> handleWaitingListNotFoundException(WaitingListNotFoundException exception, WebRequest request) {
		log.error("WaitingList not found ", exception);

		return handleGlobal(exception, request, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity<?> handleCategoryNotFoundException(CategoryNotFoundException exception, WebRequest request) {
		log.error("Category not found ", exception);

		return handleGlobal(exception, request, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AuthenticationFailedException.class)
	public ResponseEntity<?> handleAuthenticationFailedException(AuthenticationFailedException exception, WebRequest request) {
		log.error("Authentication failed ", exception);

		return handleGlobal(exception, request, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException exception, WebRequest request) {
		log.error("Access denied ", exception);

		return handleGlobal(exception, request, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleResourceNotFoundException(MethodArgumentNotValidException exception, WebRequest request) {
		log.error(exception.getMessage());
		List<ObjectError> allErrors = exception.getBindingResult().getAllErrors();

		String collect = allErrors.stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage)
				.collect(Collectors.joining(","));

		ErrorDetails errorDetails = new ErrorDetails(new Date(), collect,
				request.getDescription(false));

		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<?> handleGlobal(Exception exception, WebRequest request, HttpStatus httpStatus) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));

		return new ResponseEntity<>(errorDetails, httpStatus);
	}
}

