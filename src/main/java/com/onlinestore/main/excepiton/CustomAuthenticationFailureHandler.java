package com.onlinestore.main.excepiton;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException {
		String errorMessage = "You're not logged in";

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().write(errorMessage);
	}
}
