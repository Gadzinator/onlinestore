package com.onlinestore.main.config;

import com.onlinestore.main.aop.TransactionAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.sql.DataSource;

@Configuration
@EnableAspectJAutoProxy
public class TransactionAspectConfig {

	@Bean
	public TransactionAspect transactionAspect(ConnectionHolder connectionHolder) {
		return new TransactionAspect(connectionHolder);
	}

	@Bean
	public ConnectionHolder connectionHolder(DataSource dataSource) {
		return new ConnectionHolder(dataSource);
	}
}
