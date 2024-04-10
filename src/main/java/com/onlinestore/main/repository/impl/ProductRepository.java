package com.onlinestore.main.repository.impl;

import com.onlinestore.main.config.ConnectionHolder;
import com.onlinestore.main.domain.entity.Category;
import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.repository.IProductRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
public class ProductRepository implements IProductRepository {

	private final ConnectionHolder connectionHolder;
	private static final String INSERT_PRODUCT = "INSERT INTO onlinestore.product (id, name, brand, description, category_id, price, created, " +
			"is_available, \"received\") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_PRODUCT = "UPDATE onlinestore.product SET id = ?, name = ?, brand = ?, description = ?, category_id = ?, " +
			"price =?, created = ?, is_available = ?, received = ? WHERE id = ?";
	private static final String SELECT_FIND_CATEGORY_BY_ID = "SELECT category.name FROM onlinestore.category WHERE category.id = ?";
	private static final String SELECT_FIND_CATEGORY_ID_BY_CATEGORY_NAME = "SELECT category.id FROM onlinestore.category WHERE category.name = ?";
	private static final String SELECT_PRODUCT_FIND_BY_ID = "SELECT * FROM onlinestore.product WHERE product.id = ?";
	private static final String DELETE_PRODUCT_BY_ID = "DELETE FROM onlinestore.product WHERE id = ? AND NOT EXISTS " +
			"(SELECT 1 FROM onlinestore.order_product WHERE product_id = ?)";

	public ProductRepository(ConnectionHolder connectionHolder) {
		this.connectionHolder = connectionHolder;
	}

	@Override
	public void add(Product product) {
		try {
			final Connection connection = connectionHolder.getConnection();

			try (PreparedStatement statement = connection.prepareStatement(INSERT_PRODUCT)) {
				statement.setLong(1, product.getId());
				statement.setString(2, product.getName());
				statement.setString(3, product.getBrand());
				statement.setString(4, product.getDescription());

				Category category = product.getCategory();
				long categoryId = findCategoryIdByCategoryName(category);

				statement.setLong(5, categoryId);
				statement.setInt(6, product.getPrice());
				statement.setDate(7, Date.valueOf(product.getCreated()));
				statement.setBoolean(8, product.isAvailable());
				statement.setDate(9, Date.valueOf(product.getReceived()));
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Optional<Product> findById(long id) {
		try {
			Connection connection = connectionHolder.getConnection();
			PreparedStatement statement = connection.prepareStatement(SELECT_PRODUCT_FIND_BY_ID);
			statement.setLong(1, id);

			Product product = new Product();
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					product.setId(resultSet.getLong("id"));
					product.setName(resultSet.getString("name"));
					product.setBrand(resultSet.getString("brand"));
					product.setDescription(resultSet.getString("description"));

					long categoryId = resultSet.getLong("category_id");

					Category category = findCategoryById(categoryId);
					product.setCategory(category);
					product.setPrice(resultSet.getInt("price"));
					product.setCreated(resultSet.getDate("created").toLocalDate());
					product.setAvailable(resultSet.getBoolean("is_available"));
					product.setReceived(resultSet.getDate("received").toLocalDate());
				}

				return Optional.of(product);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void updateById(long id, Product updateProduct) {
		try {
			Connection connection = connectionHolder.getConnection();
			try (PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT)) {
				statement.setLong(1, updateProduct.getId());
				statement.setString(2, updateProduct.getName());
				statement.setString(3, updateProduct.getBrand());
				statement.setString(4, updateProduct.getDescription());

				long categoryId = findCategoryIdByCategoryName(updateProduct.getCategory());

				statement.setLong(5, categoryId);
				statement.setInt(6, updateProduct.getPrice());
				statement.setDate(7, Date.valueOf(updateProduct.getCreated()));
				statement.setBoolean(8, updateProduct.isAvailable());
				statement.setDate(9, Date.valueOf(updateProduct.getReceived()));
				statement.setLong(10, updateProduct.getId());
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void delete(long id) {
		try {
			Connection connection = connectionHolder.getConnection();
			try (PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT_BY_ID)) {
				statement.setLong(1, id);
				statement.setLong(2, id);
				final int i = statement.executeUpdate();
				System.out.println("Delete = " + i);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private Category findCategoryById(long id) throws SQLException {
		Connection connection = connectionHolder.getConnection();
		try (PreparedStatement statement = connection.prepareStatement(SELECT_FIND_CATEGORY_BY_ID)) {
			statement.setLong(1, id);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					String categoryName = resultSet.getString("name");
					return Category.valueOf(categoryName);
				} else {
					throw new IllegalArgumentException("Category with this id " + id + " was not found");
				}
			}
		}
	}

	private long findCategoryIdByCategoryName(Category category) throws SQLException {
		Connection connection = connectionHolder.getConnection();
		try (PreparedStatement statement = connection.prepareStatement(SELECT_FIND_CATEGORY_ID_BY_CATEGORY_NAME)) {
			statement.setString(1, category.name());

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getLong("id");
				} else {
					throw new IllegalArgumentException("Id with this name " + category.name() + " was not found");
				}
			}
		}
	}
}
