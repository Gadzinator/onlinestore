package com.onlinestore.main.repository.impl;

import com.onlinestore.main.dao.AbstractDao;
import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.domain.entity.WaitingList;
import com.onlinestore.main.domain.entity.WaitingList_;
import com.onlinestore.main.repository.IWaitingListRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class WaitingListRepository extends AbstractDao<WaitingList, Long> implements IWaitingListRepository {

	@PersistenceContext
	private EntityManager entityManager;

	protected WaitingListRepository() {
		super(WaitingList.class);
	}

	@Override
	public Optional<WaitingList> findByProduct(Product product) {
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<WaitingList> criteriaQuery = criteriaBuilder.createQuery(WaitingList.class);
		final Root<WaitingList> root = criteriaQuery.from(WaitingList.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get(WaitingList_.product), product));

		try {
			final WaitingList result = entityManager.createQuery(criteriaQuery).getSingleResult();
			return Optional.ofNullable(result);
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}
}
