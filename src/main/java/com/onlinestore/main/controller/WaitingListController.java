package com.onlinestore.main.controller;

import com.onlinestore.main.service.IWaitingListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/waitingLists")
public class WaitingListController {

	private final IWaitingListService waitingListService;

	public WaitingListController(IWaitingListService waitingListService) {
		this.waitingListService = waitingListService;
	}

	@PostMapping("/{productId}/{username}")
	public ResponseEntity<?> add(@PathVariable(value = "productId") Long productId, @PathVariable(value = "username") String username) {
		waitingListService.add(productId, username);

		return new ResponseEntity<>("waitingList add", HttpStatus.CREATED);
	}
}
