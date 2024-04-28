package com.onlinestore.main.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:/application.properties")
public class LiquibaseConfig {

	private final DataSource dataSource;

	@Value("${liquibase.change-log}")
	private String changelog;

	public LiquibaseConfig(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Bean
	public SpringLiquibase liquibase() {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog(changelog);

		return liquibase;
	}
}
