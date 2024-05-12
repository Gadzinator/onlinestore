package com.onlinestore.main.controller;

import com.onlinestore.main.domain.dto.UserDto;
import com.onlinestore.main.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

	private final IUserService userService;

	@GetMapping("/id/{id}")
	public ResponseEntity<UserDto> findById(@PathVariable(value = "id") Long id) {
		return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
	}

	@GetMapping("/{page}/{size}")
	public ResponseEntity<List<UserDto>> findAll(@PathVariable("page") int page, @PathVariable("size") int size) {
		final Pageable pageable = PageRequest.of(page, size);
		Page<UserDto> usersPage = userService.findAll(pageable);

		return new ResponseEntity<>(usersPage.getContent(), HttpStatus.OK);
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<UserDto> findByName(@PathVariable(value = "name") String name) {
		return new ResponseEntity<>(userService.findByName(name), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable(value = "id") Long id) {
		userService.deleteById(id);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
