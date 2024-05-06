package com.onlinestore.main.service;

import com.onlinestore.main.domain.dto.ProductDto;

import java.util.List;

public interface IProductService {
	void add(ProductDto productDto);

	ProductDto findById(long id);

	List<ProductDto> findAll();

	ProductDto findByName(String name);

	void update(ProductDto updateProductDto);

	void deleteByID(long id);
}
