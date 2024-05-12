package com.onlinestore.main.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

	public void save(T entity) {
		entityManager.persist(entity);
	}

	public List<T> findAll() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
		final Root<T> root = criteriaQuery.from(entityClass);
		criteriaQuery.select(root);

		return entityManager.createQuery(criteriaQuery).getResultList();
	}

	public Page<T> findAll(Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
		Root<T> root = criteriaQuery.from(entityClass);
		criteriaQuery.select(root);

		TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);

		if (pageable != null) {
			typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
			typedQuery.setMaxResults(pageable.getPageSize());

			List<T> resultList = typedQuery.getResultList();
			long totalCount = countTotal();

			return new PageImpl<>(resultList, pageable, totalCount);
		} else {
			return new PageImpl<>(typedQuery.getResultList());
		}
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

	private long countTotal() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		Root<T> root = countQuery.from(entityClass);
		countQuery.select(criteriaBuilder.count(root));

		TypedQuery<Long> typedQuery = entityManager.createQuery(countQuery);

		return typedQuery.getSingleResult();
	}
}
