package com.onlinestore.main.repository.impl;

import com.onlinestore.main.repository.AbstractDao;
import com.onlinestore.main.domain.entity.User;
import com.onlinestore.main.repository.IUserRepository;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

	@Override
	public Optional<User> findUserByEmail(String email) {
		final EntityGraph<User> entityGraph = entityManager.createEntityGraph(User.class);
		entityGraph.addAttributeNodes("role");

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
		Root<User> root = criteriaQuery.from(User.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get("email"), email));

		TypedQuery<User> typedQuery = entityManager.createQuery(criteriaQuery);
		typedQuery.setHint("javax.persistence.fetchgraph", entityGraph);

		try {
			User user = typedQuery.getSingleResult();
			return Optional.ofNullable(user);
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}
}
