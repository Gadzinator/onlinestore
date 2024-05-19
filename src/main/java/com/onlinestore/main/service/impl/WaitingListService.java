package com.onlinestore.main.service.impl;

import com.onlinestore.main.domain.dto.ProductDto;
import com.onlinestore.main.domain.dto.WaitingLIstDto;
import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.domain.entity.User;
import com.onlinestore.main.domain.entity.WaitingList;
import com.onlinestore.main.exception.ProductNotFoundException;
import com.onlinestore.main.exception.UserNotFoundException;
import com.onlinestore.main.exception.WaitingListNotFoundException;
import com.onlinestore.main.mapper.IWaitingListMapper;
import com.onlinestore.main.repository.impl.ProductRepository;
import com.onlinestore.main.repository.impl.UserRepository;
import com.onlinestore.main.repository.impl.WaitingListRepository;
import com.onlinestore.main.service.IWaitingListService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@AllArgsConstructor
@Service
public class WaitingListService implements IWaitingListService {

	private final WaitingListRepository waitingListRepository;

	private final ProductRepository productRepository;

	private final UserRepository userRepository;

	private final IWaitingListMapper waitingListMapper;


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

	@Override
	public WaitingLIstDto findById(long id) {
		log.info("Starting finding waiting list by id: " + id);

		final WaitingList waitingList = waitingListRepository.findById(id).orElseThrow(
				() -> new WaitingListNotFoundException("Waiting list not found by id: " + id));

		log.info("Finished finding waiting list by id: " + waitingList);

		return waitingListMapper.mapToWaitingListDto(waitingList);
	}

	@Override
	public Page<WaitingLIstDto> findAll(Pageable waitingListPageable) {
		log.info("Starting finding all waiting list: " + waitingListPageable);
		final Page<WaitingList> waitingListPage = waitingListRepository.findAll(waitingListPageable);

		if (waitingListPage.isEmpty()) {
			throw new WaitingListNotFoundException("Waiting list not found");
		}

		log.info("Finished finding all waiting list: " + waitingListPageable);

		return waitingListPage.map(waitingListMapper::mapToWaitingListDto);
	}

	@Transactional
	@Override
	public WaitingLIstDto update(WaitingLIstDto waitingLIstDto) {
		log.info("Starting update of waiting list: " + waitingLIstDto);

		WaitingList waitingList = updateAllFields(waitingLIstDto);

		waitingListRepository.update(waitingList);

		log.info("Finished updating waiting list: " + waitingList);

		return waitingListMapper.mapToWaitingListDto(waitingList);
	}

	@Transactional
	@Override
	public void delete(long id) {
		waitingListRepository.deleteById(id);
	}

	private User findUserByName(String name) {
		log.info("Starting finding user by name: " + name);

		final User user = userRepository.findByName(name)
				.orElseThrow(() -> new UserNotFoundException("User was not found by name " + name));

		log.info("Finished finding user by name: " + user);

		return user;
	}

	private Product findProductById(long id) {
		log.info("Starting finding product by id: " + id);

		final Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found by id: " + id));

		log.info("Finished finding product by id: " + product);

		return product;
	}


	private WaitingList updateAllFields(WaitingLIstDto updateWaitingList) {
		log.info("Starting update all fields: " + updateWaitingList);

		final WaitingLIstDto waitingLIstDto = findById(updateWaitingList.getId());
		final WaitingList waitingList = waitingListMapper.mapToWaitingList(waitingLIstDto);

		final User user = findUserByName(updateWaitingList.getUsername());
		final ProductDto productDto = waitingLIstDto.getProductDto();
		final Product product = findProductById(productDto.getId());

		waitingList.setUser(user);
		waitingList.setProduct(product);

		log.info("Finished updating all fields for waiting list: " + waitingList);

		return waitingList;
	}
}
