package com.onlinestore.main.repository.impl;

import com.onlinestore.main.config.ConnectionHolder;
import com.onlinestore.main.domain.entity.Order;
import com.onlinestore.main.domain.entity.OrderStatus;
import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.repository.IOrderRepository;
import com.onlinestore.main.repository.IProductRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository implements IOrderRepository {

	private final ConnectionHolder connectionHolder;
	private final IProductRepository productRepository;
	private static final String INSERT_ORDER = "INSERT INTO onlinestore.\"order\" (id, created, order_status_id) VALUES (?, ?, ?)";
	private static final String INSERT_ORDER_PRODUCT = "INSERT INTO onlinestore.order_product (order_id, product_id) VALUES (?, ?)";
	private static final String UPDATE_ORDER = "UPDATE onlinestore.order SET id = ?, created = ?, order_status_id = ? WHERE id = ?";
	private static final String UPDATE_ORDER_PRODUCT = "UPDATE onlinestore.order_product SET order_id = ?, product_id = ? WHERE order_id = ?";
	private static final String SELECT_GET_ORDER_STATUS_BY_ID = "SELECT name FROM onlinestore.order_status WHERE id = ?";
	private static final String SELECT_FIND_ORDER_STATUS_BY_NAME = "SELECT id FROM onlinestore.order_status WHERE name = ?";
	private static final String SELECT_ORDER_FIND_BY_ID = "SELECT * FROM onlinestore.\"order\" WHERE \"order\".id = ?";
	private static final String SELECT_ORDER_PRODUCT_BY_ID = "SELECT order_product.product_id FROM onlinestore.order_product " +
			"WHERE order_product.order_id = ?";
	private static final String DELETE_ORDER_PRODUCT_BY_ORDER_ID = "DELETE FROM onlinestore.order_product WHERE order_id = ?";
	private static final String DELETE_ORDER_PRODUCT = "DELETE FROM onlinestore.order_product WHERE order_id = ? AND product_id = ?";
	private static final String DELETE_ORDER_BY_ID = "DELETE FROM onlinestore.\"order\" WHERE id = ?";

	public OrderRepository(ConnectionHolder connectionHolder, IProductRepository productRepository) {
		this.connectionHolder = connectionHolder;
		this.productRepository = productRepository;
	}

	@Override
	public void add(Order order) {
		try {
			Connection connection = connectionHolder.getConnection();
			try (PreparedStatement orderStatement = connection.prepareStatement(INSERT_ORDER);
				 PreparedStatement orderProductStatement = connection.prepareStatement(INSERT_ORDER_PRODUCT);
				 PreparedStatement orderStatusStatement = connection.prepareStatement(SELECT_FIND_ORDER_STATUS_BY_NAME)) {

				orderStatement.setLong(1, order.getId());
				orderStatement.setDate(2, Date.valueOf(order.getCreated()));
				String orderStatusName = order.getOrderStatus().getValue();

				orderStatusStatement.setString(1, orderStatusName);
				long orderStatusId;
				try (ResultSet resultSet = orderStatusStatement.executeQuery();) {
					if (resultSet.next()) {
						orderStatusId = resultSet.getLong("id");
					} else {
						throw new IllegalArgumentException("Order status not found: " + orderStatusName);
					}
				}

				orderStatement.setLong(3, orderStatusId);
				orderStatement.executeUpdate();

				for (Product product : order.getProducts()) {
					orderProductStatement.setLong(1, order.getId());
					orderProductStatement.setLong(2, product.getId());
					orderProductStatement.executeUpdate();
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Optional<Order> findById(long id) {
		try {
			Connection connection = connectionHolder.getConnection();
			PreparedStatement statement = connection.prepareStatement(SELECT_ORDER_FIND_BY_ID);
			statement.setLong(1, id);
			try (ResultSet resultSet = statement.executeQuery()) {

				Order order = new Order();
				if (resultSet.next()) {
					order.setId(resultSet.getLong("id"));
					order.setCreated(resultSet.getDate("created").toLocalDate());

					long productId = findProductById(id);
					Optional<Product> optionalProduct = productRepository.findById(productId);

					OrderStatus orderStatus = null;
					if (optionalProduct.isPresent()) {
						Product product = optionalProduct.get();
						order.setProducts(List.of(product));
						long statusId = resultSet.getLong("order_status_id");
						orderStatus = findOrderStatusById(statusId);
					}
					order.setOrderStatus(orderStatus);
				}

				return Optional.of(order);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void updateById(long id, Order updateOrder) {
		try {
			Connection connection = connectionHolder.getConnection();

			try (PreparedStatement statement = connection.prepareStatement(UPDATE_ORDER)) {
				Optional<Order> optionalOrder = findById(id);
				Order existingOrder = null;
				if (optionalOrder.isPresent()) {
					existingOrder = optionalOrder.get();
					existingOrder.setCreated(updateOrder.getCreated());
					existingOrder.setOrderStatus(updateOrder.getOrderStatus());
					existingOrder.setProducts(updateOrder.getProducts());
				}

				String orderStatusName = updateOrder.getOrderStatus().getValue();
				long orderStatusId = findIdOrderStatusByName(orderStatusName);

				statement.setLong(1, updateOrder.getId());
				statement.setDate(2, Date.valueOf(existingOrder.getCreated()));
				statement.setLong(3, orderStatusId);
				statement.setLong(4, updateOrder.getId());

				updateOrderProduct(id, updateOrder);
				final int i = statement.executeUpdate();
				System.out.println("Execute = " + i);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void deleteById(long id) {
		try {
			Connection connection = connectionHolder.getConnection();

			try (PreparedStatement orderStatement = connection.prepareStatement(DELETE_ORDER_BY_ID);
				 PreparedStatement orderProductStatement = connection.prepareStatement(DELETE_ORDER_PRODUCT_BY_ORDER_ID)) {
				orderProductStatement.setLong(1, id);
				orderProductStatement.executeUpdate();
				orderStatement.setLong(1, id);
				orderStatement.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private OrderStatus findOrderStatusById(long id) throws SQLException {
		Connection connection = connectionHolder.getConnection();
		try (PreparedStatement statement = connection.prepareStatement(SELECT_GET_ORDER_STATUS_BY_ID)) {
			statement.setLong(1, id);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					String statusName = resultSet.getString("name");

					return OrderStatus.valueOf(statusName);
				} else {
					throw new IllegalArgumentException("Order status not found for id: " + id);
				}
			}
		}
	}

	private long findProductById(long id) throws SQLException {
		Connection connection = connectionHolder.getConnection();

		try (PreparedStatement statement = connection.prepareStatement(SELECT_ORDER_PRODUCT_BY_ID)) {
			statement.setLong(1, id);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getLong("product_id");

				} else {
					throw new IllegalArgumentException("Product with ID " + id + " not found");
				}
			}
		}
	}

	private void updateOrderProduct(long orderId, Order updateOrder) throws SQLException {
		Connection connection = connectionHolder.getConnection();
		try (PreparedStatement deleteStatement = connection.prepareStatement(DELETE_ORDER_PRODUCT);
			 PreparedStatement updateStatement = connection.prepareStatement(UPDATE_ORDER_PRODUCT)) {
			for (Product product : updateOrder.getProducts()) {
				deleteStatement.setLong(1, orderId);
				deleteStatement.setLong(2, product.getId());
				deleteStatement.executeUpdate();

				updateStatement.setLong(1, orderId);
				updateStatement.setLong(2, product.getId());
				updateStatement.setLong(3, updateOrder.getId());
				updateStatement.executeUpdate();
			}
		}
	}

	private long findIdOrderStatusByName(String orderStatusName) throws SQLException {
		Connection connection = connectionHolder.getConnection();
		try (PreparedStatement statement = connection.prepareStatement(SELECT_FIND_ORDER_STATUS_BY_NAME)) {
			statement.setString(1, orderStatusName);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getLong("id");
				} else {
					throw new IllegalArgumentException("Order status not found: " + orderStatusName);
				}
			}
		}
	}
}
