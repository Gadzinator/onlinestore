package com.onlinestore.main.controller.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@WebAppConfiguration
@ComponentScan(basePackages = {"com.onlinestore"},
		excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX,
				pattern = "com.onlinestore.config")})
public class WebMvcConfig implements WebMvcConfigurer {
}
