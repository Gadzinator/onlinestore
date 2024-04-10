package com.onlinestore.main.service.impl;

import com.onlinestore.main.annotation.Transaction;
import com.onlinestore.main.domain.dto.ProductDto;
import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.mapper.IProductMapper;
import com.onlinestore.main.repository.IProductRepository;
import com.onlinestore.main.service.IProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ProductService implements IProductService {

	private IProductRepository productRepository;
	private IProductMapper productMapper;

	@Transaction
	@Override
	public void add(ProductDto productDto) {
		Product product = productMapper.mapToProduct(productDto);
		productRepository.add(product);
	}

	@Override
	public ProductDto findById(long id) {
		return productRepository.findById(id)
				.map(product -> productMapper.mapToProductDto(product))
				.orElseThrow(() -> new NoSuchElementException(
						String.format("Product with id %d was not found", id)));
	}

	@Transaction
	@Override
	public void updateById(long id, ProductDto updateProductDto) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException(
						String.format("Product with id %d was not found", id)));

		productRepository.updateById(product.getId(), productMapper.mapToProduct(updateProductDto));
	}

	@Transaction
	@Override
	public void deleteByID(long id) {
		ProductDto productDto = findById(id);
		if (productDto != null) {
			productRepository.delete(id);
		}
	}
}
