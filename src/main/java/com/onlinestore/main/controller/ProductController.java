package com.onlinestore.main.controller;

import com.onlinestore.main.domain.dto.ProductDto;
import com.onlinestore.main.service.IProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

	private final IProductService productService;

	public ProductController(IProductService productService) {
		this.productService = productService;
	}

	@PostMapping
	public ResponseEntity<?> add(@RequestBody(required = false) @Valid ProductDto productDto) {
		if (productDto == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		productService.add(productDto);

		return new ResponseEntity<>("Product add", HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<ProductDto>> findAll() {
		return new ResponseEntity<>(productService.findAll(), HttpStatus.OK);
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<?> findById(@PathVariable(value = "id") Long id) {
		ProductDto productDto = productService.findById(id);

		return new ResponseEntity<>(productDto, HttpStatus.OK);
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<?> findByName(@PathVariable(value = "name") String name) {
		return new ResponseEntity<>(productService.findByName(name), HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<?> update(@RequestBody ProductDto updateProductDto) {
		productService.update(updateProductDto);

		return new ResponseEntity<>(updateProductDto, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteByID(@PathVariable("id") Long id) {
		productService.deleteByID(id);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
