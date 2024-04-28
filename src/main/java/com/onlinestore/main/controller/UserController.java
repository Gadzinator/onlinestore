package com.onlinestore.main.controller;

import com.onlinestore.main.domain.dto.RegistrationUserDto;
import com.onlinestore.main.domain.dto.UserDto;
import com.onlinestore.main.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

	private final IUserService userService;

	@PostMapping("/registration")
	public ResponseEntity<?> createNewUser(@RequestBody(required = false) RegistrationUserDto registrationUserDto) {
		if (registrationUserDto == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(userService.createNewUser(registrationUserDto), HttpStatus.CREATED);
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<UserDto> findById(@PathVariable(value = "id") Long id) {
		return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<UserDto>> findAll() {
		return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
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
