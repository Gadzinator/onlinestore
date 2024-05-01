package com.onlinestore.main.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.onlinestore"},
		excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX,
				pattern = "com.onlinestore.config")})
public class AppConfig {
}
