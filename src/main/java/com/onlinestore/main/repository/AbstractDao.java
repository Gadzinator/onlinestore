package com.onlinestore.main.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<T, PK extends Serializable> {

	@PersistenceContext
	private EntityManager entityManager;
	private final Class<T> entityClass;

	protected AbstractDao(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public void add(T entity) {
		entityManager.persist(entity);
	}

	public List<T> findAll() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
		final Root<T> root = criteriaQuery.from(entityClass);
		criteriaQuery.select(root);

		return entityManager.createQuery(criteriaQuery).getResultList();
	}

	public void update(T entity) {
		entityManager.merge(entity);
	}

	public void deleteById(PK id) {
		T entityToRemove = entityManager.find(entityClass, id);
		if (entityToRemove != null) {
			entityManager.remove(entityToRemove);
		}
	}

	public Optional<T> findById(PK id) {
		final T entity = entityManager.find(entityClass, id);
		return Optional.ofNullable(entity);
	}
}
