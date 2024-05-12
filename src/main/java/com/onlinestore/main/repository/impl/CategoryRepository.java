package com.onlinestore.main.repository.impl;


import com.onlinestore.main.domain.entity.Category;
import com.onlinestore.main.domain.entity.Category_;
import com.onlinestore.main.repository.AbstractDao;
import com.onlinestore.main.repository.ICategoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CategoryRepository extends AbstractDao<Category, Long> implements ICategoryRepository {

	@PersistenceContext
	private EntityManager entityManager;

	protected CategoryRepository() {
		super(Category.class);
	}

	@Override
	public Optional<Category> findByName(String name) {
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Category> criteriaQuery = criteriaBuilder.createQuery(Category.class);
		final Root<Category> root = criteriaQuery.from(Category.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get(Category_.name), name));

		try {
			final Category result = entityManager.createQuery(criteriaQuery).getSingleResult();
			return Optional.ofNullable(result);
		} catch (
				NoResultException e) {
			return Optional.empty();
		}
	}
}
