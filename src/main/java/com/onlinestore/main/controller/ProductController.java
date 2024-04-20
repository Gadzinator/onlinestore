package com.onlinestore.main.controller;

import com.onlinestore.main.controller.utils.JsonUtils;
import com.onlinestore.main.domain.dto.ProductDto;
import com.onlinestore.main.domain.dto.UserDto;
import com.onlinestore.main.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProductController {

	private final IProductService productService;
	private final JsonUtils jsonUtils;

	public ProductController(IProductService productService, JsonUtils jsonUtils) {
		this.productService = productService;
		this.jsonUtils = jsonUtils;
	}

	public void add(ProductDto productDto) {
		productService.add(productDto);

		String json = jsonUtils.getJson(productDto);
		System.out.println("Method to add to ProductController - " + json);
	}

	public List<ProductDto> findAll() {
		final List<ProductDto> productDtoList = productService.findAll();
		for (ProductDto productDto : productDtoList) {
			final String json = jsonUtils.getJson(productDto);
			System.out.println("Method to findAll to ProductController - " + json);
		}

		return productDtoList;
	}

	public ProductDto findById(long id) {
		ProductDto productDto = productService.findById(id);

		String json = jsonUtils.getJson(productDto);
		System.out.println("Method to findById to ProductController - " + json);

		return productDto;
	}

	public ProductDto findByName(String name) {
		final ProductDto productDto = productService.findByName(name);

		String json = jsonUtils.getJson(productDto);
		System.out.println("Method to findByName to ProductController - " + json);

		return productDto;
	}

	public void update(ProductDto updateProductDto) {
		productService.update(updateProductDto);

		String json = jsonUtils.getJson(updateProductDto);
		System.out.println("Method to update to ProductController " + json);
	}

	public void deleteByID(long id) {
		productService.deleteByID(id);

		Map<String, Object> response = new HashMap<>();
		response.put("Message", "Product with Id " + id + " has been successfully deleted");
		String json = jsonUtils.getJson(response);
		System.out.println(json);
	}
}
