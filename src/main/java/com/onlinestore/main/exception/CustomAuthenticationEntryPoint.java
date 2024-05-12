package com.onlinestore.main.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private static final Logger logger = LogManager.getLogger(CustomAuthenticationEntryPoint.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		String message = "Unauthorized";
		String username = request.getRemoteUser();
		if (username != null && !username.isEmpty()) {
			message += ", " + username + " is not authenticated";
			logger.warn(message + "url" + request.getRequestURI());
		} else {
			message += ", Anonymous user is not authenticated";
			logger.warn(message + ", url" + request.getRequestURI());
		}

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().write(message);
	}
}
