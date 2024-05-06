package com.onlinestore.main.service.impl;

import com.onlinestore.main.domain.dto.JwtRequest;
import com.onlinestore.main.domain.dto.MyUserPrincipal;
import com.onlinestore.main.excepiton.AuthenticationFailedException;
import com.onlinestore.main.excepiton.UserNotFoundException;
import com.onlinestore.main.repository.impl.UserRepository;
import com.onlinestore.main.service.IAuthService;
import com.onlinestore.main.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

	private static final Logger logger = LogManager.getLogger(AuthService.class);

	private final JwtTokenUtils jwtTokenUtils;

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	@Override
	public String createAuthToken(JwtRequest authRequest) {
		logger.info("In userService createAuthToken " + authRequest);
		UserDetails userDetails = loadUserByUsername(authRequest.getUserName());
		if (!passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword())) {
			throw new AuthenticationFailedException("Password not valid");
		}

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, authRequest.getPassword(), userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final boolean authenticated = authentication.isAuthenticated();
		if (!authenticated) {
			throw new AuthenticationFailedException("Authentication failed");
		}

		return jwtTokenUtils.generateToken(userDetails);
	}

	@Override
	public UserDetails loadUserByUsername(String userName) {
		logger.info("In userService loadUserByUsername");
		return userRepository.findByName(userName)
				.map(MyUserPrincipal::new)
				.orElseThrow(() -> new UserNotFoundException(String.format("Username '%s' not found", userName)));
	}
}
