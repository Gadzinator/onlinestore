package com.onlinestore.main.aop;

import com.onlinestore.main.config.ConnectionHolder;
import com.onlinestore.main.excepiton.FailedTransaction;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Aspect
@Component
public class TransactionAspect {

	private final ConnectionHolder connectionHolder;

	public TransactionAspect(ConnectionHolder connectionHolder) {
		this.connectionHolder = connectionHolder;
	}

	@Around(value = "@annotation(com.onlinestore.main.annotation.Transaction)")
	public Object manageTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
		Connection connection = null;
		try {
			connection = connectionHolder.getConnection();
			Object proceed = joinPoint.proceed();
			connection.commit();

			return proceed;

		} catch (Exception e) {
			if (connection != null) {
				connection.rollback();
			}

			throw new FailedTransaction("Failed to execute transaction: " + e.getMessage(), e);

		} finally {
			connectionHolder.releaseConnection();
		}
	}
}
