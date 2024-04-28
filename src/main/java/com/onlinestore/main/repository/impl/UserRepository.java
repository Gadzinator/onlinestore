package com.onlinestore.main.repository.impl;

import com.onlinestore.main.dao.AbstractDao;
import com.onlinestore.main.domain.entity.User;
import com.onlinestore.main.repository.IUserRepository;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository extends AbstractDao<User, Long> implements IUserRepository {

	@PersistenceContext
	private EntityManager entityManager;

	protected UserRepository() {
		super(User.class);
	}

	@Override
	public Optional<User> findByName(String name) {
		final EntityGraph<User> entityGraph = entityManager.createEntityGraph(User.class);
		entityGraph.addAttributeNodes("role");
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
		Root<User> root = criteriaQuery.from(User.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get("name"), name));

		TypedQuery<User> typedQuery = entityManager.createQuery(criteriaQuery);
		typedQuery.setHint("jakarta.persistence.fetchgraph", entityGraph);

		try {
			User user = typedQuery.getSingleResult();
			return Optional.ofNullable(user);
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}
}
