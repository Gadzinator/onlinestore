package com.onlinestore.main.repository.impl;

import com.onlinestore.main.dao.AbstractDao;
import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.domain.entity.Product_;
import com.onlinestore.main.repository.IProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProductRepositoryDao extends AbstractDao<Product, Long> implements IProductRepository {

	@PersistenceContext
	private EntityManager entityManager;

	protected ProductRepositoryDao() {
		super(Product.class);
	}

	@Override
	public Optional<Product> findByName(String name) {
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
		final Root<Product> root = criteriaQuery.from(Product.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get(Product_.name), name));

		try {
			final Product result = entityManager.createQuery(criteriaQuery).getSingleResult();
			return Optional.ofNullable(result);
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}
}
