package com.onlinestore.main.controller.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@WebAppConfiguration
@ComponentScan(basePackages = "com.onlinestore")
public class WebMvcConfig implements WebMvcConfigurer {
}
