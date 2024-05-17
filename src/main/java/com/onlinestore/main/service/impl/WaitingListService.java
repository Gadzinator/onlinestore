package com.onlinestore.main.service.impl;

import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.domain.entity.User;
import com.onlinestore.main.domain.entity.WaitingList;
import com.onlinestore.main.exception.ProductNotFoundException;
import com.onlinestore.main.exception.UserNotFoundException;
import com.onlinestore.main.repository.impl.ProductRepository;
import com.onlinestore.main.repository.impl.UserRepository;
import com.onlinestore.main.repository.impl.WaitingListRepository;
import com.onlinestore.main.service.IWaitingListService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
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
	public void save(long productId, String username) {
		log.info("Starting adding waitingList: " + productId + ", and " + username);

		Product product = findProductById(productId);
		final User user = findUserByName(username);

		final Optional<WaitingList> optionalWaitingList = waitingListRepository.findByProduct(product);
		final WaitingList waitingList;

		if (optionalWaitingList.isPresent()) {
			waitingList = optionalWaitingList.get();
			waitingList.setUser(user);

			log.info("WaitingList updated for product: " + productId + ", and user: " + username);
		} else {
			waitingList = new WaitingList();
			waitingList.setProduct(product);
			waitingList.setUser(user);

			log.info("New WaitingList created for product: " + productId + ", and user: " + username);
		}

		waitingListRepository.save(waitingList);

		log.info("Finished waitingList added successfully: " + waitingList);
	}

	private Product findProductById(long id) {
		log.info("Starting finding product by id: " + id);

		final Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found by id: " + id));

		log.info("Finished finding product by id: " + product);

		return product;
	}

	private User findUserByName(String name) {
		log.info("Starting finding user by name: " + name);

		final User user = userRepository.findByName(name)
				.orElseThrow(() -> new UserNotFoundException("User was not found by name " + name));

		log.info("Finished finding user by name: " + user);

		return user;
	}
}
