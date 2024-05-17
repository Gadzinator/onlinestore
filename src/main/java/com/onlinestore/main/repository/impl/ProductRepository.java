package com.onlinestore.main.repository.impl;

import com.onlinestore.main.repository.AbstractDao;
import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.domain.entity.Product_;
import com.onlinestore.main.repository.IProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ProductRepository extends AbstractDao<Product, Long> implements IProductRepository {

	@PersistenceContext
	private EntityManager entityManager;

	protected ProductRepository() {
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

	@Override
	public List<Product> findByParams(Map<String, String> params) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		CriteriaQuery<Product> query = criteriaBuilder.createQuery(Product.class);
		Root<Product> root = query.from(Product.class);
		List<Predicate> predicates = new ArrayList<>();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			Predicate name = criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue());
			predicates.add(criteriaBuilder.and(name));
		}

		query.select(root).where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
		return entityManager.createQuery(query).getResultList();
	}
}
