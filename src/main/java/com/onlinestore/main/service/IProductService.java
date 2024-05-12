package com.onlinestore.main.service;

import com.onlinestore.main.domain.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService {
	void add(ProductDto productDto);

	ProductDto findById(long id);

	Page<ProductDto> findAll(Pageable pageable);

	ProductDto findByName(String name);

	void update(ProductDto updateProductDto);

	void deleteByID(long id);
}
