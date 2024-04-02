package com.onlinestore.main.controller;

import com.onlinestore.main.controller.utils.JsonUtils;
import com.onlinestore.main.domain.dto.ProductDto;
import com.onlinestore.main.service.IProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Controller
public class ProductController {

	private IProductService productService;

	public void add(ProductDto productDto) {
		productService.add(productDto);

		String json = JsonUtils.getJson(productDto);
		System.out.println("Method to add to ProductController - " + json);
	}

	public ProductDto findById(long id) {
		ProductDto productDto = productService.findById(id);

		String json = JsonUtils.getJson(productDto);
		System.out.println("Method to findById to ProductController - " + json);

		return productDto;
	}

	public void updateById(long id, ProductDto updateProductDto) {
		productService.updateById(1, updateProductDto);

		String json = JsonUtils.getJson(updateProductDto);
		System.out.println("Method to update to ProductController " + json);
	}

	public void deleteByID(long id) {
		productService.deleteByID(id);

		Map<String, Object> response = new HashMap<>();
		response.put("Message", "Product with Id " + id + " has been successfully deleted");
		String json = JsonUtils.getJson(response);
		System.out.println(json);
	}
}
