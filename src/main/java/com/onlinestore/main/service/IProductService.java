package com.onlinestore.main.service;

import com.onlinestore.main.domain.dto.ProductDto;

public interface IProductService {
	void add(ProductDto productDto);

	ProductDto findById(long id);

	void updateById(long id, ProductDto productDto);

	void deleteByID(long id);
}
