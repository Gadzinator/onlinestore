package com.onlinestore.main.service.impl;

import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.domain.entity.User;
import com.onlinestore.main.domain.entity.WaitingList;
import com.onlinestore.main.excepiton.ProductNotFoundException;
import com.onlinestore.main.excepiton.UserNotFoundException;
import com.onlinestore.main.repository.impl.ProductRepository;
import com.onlinestore.main.repository.impl.UserRepository;
import com.onlinestore.main.repository.impl.WaitingListRepository;
import com.onlinestore.main.service.IWaitingListService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class WaitingListService implements IWaitingListService {

	private final WaitingListRepository waitingListRepository;

	private final ProductRepository productRepository;

	private final UserRepository userRepository;

	public WaitingListService(WaitingListRepository waitingListRepository, ProductRepository productRepository, UserRepository userRepository) {
		this.waitingListRepository = waitingListRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	@Override
	public void add(long productId, String username) {
		Product product = findProductById(productId);
		final User user = findUserByName(username);

		final Optional<WaitingList> optionalWaitingList = waitingListRepository.findByProduct(product);
		final WaitingList waitingList;

		if (optionalWaitingList.isPresent()) {
			waitingList = optionalWaitingList.get();
			waitingList.setUser(user);

		} else {
			waitingList = new WaitingList();
			waitingList.setProduct(product);
			waitingList.setUser(user);
		}

		waitingListRepository.add(waitingList);
	}

	private Product findProductById(long id) {
		return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
	}

	private User findUserByName(String name) {
		return userRepository.findByName(name)
				.orElseThrow(() -> new UserNotFoundException("User was not found with name " + name));
	}
}
