package com.onlinestore.main.controller;

import com.onlinestore.main.controller.utils.JsonUtils;
import com.onlinestore.main.domain.dto.OrderDto;
import com.onlinestore.main.excepiton.OrderNotFoundException;
import com.onlinestore.main.service.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
