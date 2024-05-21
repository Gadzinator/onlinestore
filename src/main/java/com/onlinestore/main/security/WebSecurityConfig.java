package com.onlinestore.main.security;

import com.onlinestore.main.exception.CustomAccessDeniedHandler;
import com.onlinestore.main.exception.CustomAuthenticationEntryPoint;
import com.onlinestore.main.exception.CustomAuthenticationFailureHandler;
import com.onlinestore.main.exception.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer {

	private final String ROLE_ADMIN = "ADMIN";

	private final JwtRequestFilter requestFilter;

	public WebSecurityConfig(JwtRequestFilter requestFilter) {
		this.requestFilter = requestFilter;

	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.cors(AbstractHttpConfigurer::disable)
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.POST, "/products/**").hasRole(ROLE_ADMIN)
						.requestMatchers("/users/**").hasRole(ROLE_ADMIN)
						.requestMatchers(HttpMethod.DELETE, "/**").hasRole(ROLE_ADMIN)
						.requestMatchers(HttpMethod.PUT, "/**").hasRole(ROLE_ADMIN)
						.requestMatchers(HttpMethod.GET, "/orders/**", "/waitingLists/{id}").authenticated()
						.requestMatchers(HttpMethod.GET, "/waitingLists/**").hasRole(ROLE_ADMIN)
						.requestMatchers("/admins/**").hasRole((ROLE_ADMIN))
						.requestMatchers(HttpMethod.POST, "/waitingLists/**", "/orders/**", "/changePassword").authenticated()
						.anyRequest().permitAll())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.formLogin(form -> form.failureHandler(authenticationFailureHandler()))
				.exceptionHandling(exceptionHandling -> exceptionHandling
						.accessDeniedHandler(accessDeniedHandler())
						.authenticationEntryPoint(authenticationEntryPoint()))
				.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
		try {
			return configuration.getAuthenticationManager();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new CustomAuthenticationEntryPoint();
	}

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new CustomAuthenticationFailureHandler();
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new CustomAuthenticationSuccessHandler();
	}
}
