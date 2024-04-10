package com.onlinestore.main.config;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ConnectionHolder {

	private final DataSource dataSource;
	private final Map<String, Connection> connectionMap = new HashMap<>();

	public ConnectionHolder(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Connection getConnection() throws SQLException {
		String threadName = Thread.currentThread().getName();
		Connection connection = connectionMap.get(threadName);

		if (connection == null || connection.isClosed()) {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			connectionMap.put(threadName, connection);
		}

		return connection;
	}

	public void releaseConnection() throws SQLException {
		String threadName = Thread.currentThread().getName();
		Connection connection = connectionMap.remove(threadName);
		if (connection != null && !connection.isClosed()) {
			connection.close();
		}
	}
}
