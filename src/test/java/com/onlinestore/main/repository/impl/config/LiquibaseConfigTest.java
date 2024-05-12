package com.onlinestore.main.repository.impl.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfigTest {

	private final DataSource dataSource;

	public LiquibaseConfigTest(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Bean
	public SpringLiquibase liquibase() {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog("changelogs/changelog-master.xml");
		liquibase.setShouldRun(true);

		return liquibase;
	}
}
