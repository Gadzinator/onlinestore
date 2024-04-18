package com.onlinestore.main.service.impl;

import com.onlinestore.main.domain.dto.ProductDto;
import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.mapper.IProductMapper;
import com.onlinestore.main.repository.impl.ProductRepositoryDao;
import com.onlinestore.main.service.IProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ProductService implements IProductService {

	private ProductRepositoryDao productRepository;
	private IProductMapper productMapper;

	@Transactional
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

	@Override
	public List<ProductDto> findAll() {
		List<ProductDto> productDtoList = new ArrayList<>();
		for (Product product : productRepository.findAll()) {
			final ProductDto productDto = productMapper.mapToProductDto(product);
			productDtoList.add(productDto);
		}

		return productDtoList;
	}

	@Override
	public ProductDto findByName(String name) {
		return productRepository.findByName(name)
				.map(product -> productMapper.mapToProductDto(product))
				.orElseThrow(() -> new NoSuchElementException(
						String.format("Product with name %s was not found", name)));
	}

	@Transactional
	@Override
	public void update(ProductDto updateProductDto) {
		productRepository.findById(updateProductDto.getId())
				.orElseThrow(() -> new NoSuchElementException(
						String.format("Product with id %d was not found", updateProductDto.getId())));

		productRepository.update(productMapper.mapToProduct(updateProductDto));
	}

	@Transactional
	@Override
	public void deleteByID(long id) {
		ProductDto productDto = findById(id);
		if (productDto != null) {
			productRepository.deleteById(id);
		}
	}
}
