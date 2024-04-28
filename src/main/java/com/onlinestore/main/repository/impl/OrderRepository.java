package com.onlinestore.main.repository.impl;

import com.onlinestore.main.dao.AbstractDao;
import com.onlinestore.main.domain.entity.Order;
import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.repository.IOrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepository extends AbstractDao<Order, Long> implements IOrderRepository {

	@PersistenceContext
	private EntityManager entityManager;

	protected OrderRepository() {
		super(Order.class);
	}

	@Override
	public List<Product> findProductsOrderId(long id) {
		return entityManager.createQuery(
						"SELECT p FROM Order o JOIN o.products p WHERE o.id = :orderId",
						Product.class)
				.setParameter("orderId", id)
				.getResultList();
	}
}
