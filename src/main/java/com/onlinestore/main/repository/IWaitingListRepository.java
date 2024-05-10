package com.onlinestore.main.repository;

import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.domain.entity.WaitingList;

import java.util.Optional;

public interface IWaitingListRepository {

	Optional<WaitingList> findByProduct(Product product);
}
