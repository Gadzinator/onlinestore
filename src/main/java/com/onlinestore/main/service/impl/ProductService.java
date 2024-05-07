package com.onlinestore.main.service.impl;

import com.onlinestore.main.domain.dto.ProductDto;
import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.excepiton.ProductNotFoundException;
import com.onlinestore.main.mapper.IProductMapper;
import com.onlinestore.main.repository.impl.ProductRepository;
import com.onlinestore.main.service.IProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService implements IProductService {

	private ProductRepository productRepository;
	private IProductMapper productMapper;

	@Transactional
	@Override
	public void add(ProductDto productDto) {
		if (productDto == null) {
			throw new NullPointerException("ProductDto cannot be null");
		}
		Product product = productMapper.mapToProduct(productDto);
		productRepository.add(product);
	}

	@Override
	public ProductDto findById(long id) {
		return productRepository.findById(id)
				.map(product -> productMapper.mapToProductDto(product))
				.orElseThrow(() -> new ProductNotFoundException("Product not was found with id " + id));
	}

	@Override
	public List<ProductDto> findAll() {
		List<ProductDto> productDtoList = new ArrayList<>();
		final List<Product> products = productRepository.findAll();
		if (!products.isEmpty()) {
			for (Product product : products) {
				final ProductDto productDto = productMapper.mapToProductDto(product);
				productDtoList.add(productDto);
			}
		} else {
			throw new ProductNotFoundException("Products were not found");
		}

		return productDtoList;
	}

	@Override
	public ProductDto findByName(String name) {
		return productRepository.findByName(name)
				.map(product -> productMapper.mapToProductDto(product))
				.orElseThrow(() -> new ProductNotFoundException("Product not was found with name " + name));
	}

	@Transactional
	@Override
	public void update(ProductDto updateProductDto) {
		productRepository.findById(updateProductDto.getId())
				.orElseThrow(() -> new ProductNotFoundException("Product not was found with id " + updateProductDto.getId()));

		productRepository.update(productMapper.mapToProduct(updateProductDto));
	}

	@Transactional
	@Override
	public void deleteByID(long id) {
		ProductDto productDto = findById(id);
		if (productDto != null) {
			productRepository.deleteById(id);
		} else {
			throw new ProductNotFoundException("Product not was found with id " + id);
		}
	}
}
