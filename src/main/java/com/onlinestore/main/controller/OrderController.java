package com.onlinestore.main.controller;

import com.onlinestore.main.domain.dto.OrderDto;
import com.onlinestore.main.service.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

	private final IOrderService orderService;

	@PostMapping
	public ResponseEntity<?> add(@RequestBody(required = false) @Valid OrderDto orderDto) {
		if (orderDto == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		orderService.add(orderDto);

		return new ResponseEntity<>("Order add", HttpStatus.CREATED);
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<OrderDto> findById(@PathVariable(value = "id") Long id) {
		return new ResponseEntity<>(orderService.findById(id), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<OrderDto>> findAll() {
		return new ResponseEntity<>(orderService.findAll(), HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<?> update(@RequestBody OrderDto orderDtoUpdate) {
		orderService.updateById(orderDtoUpdate);

		return new ResponseEntity<>(orderDtoUpdate, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
		orderService.deleteById(id);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
