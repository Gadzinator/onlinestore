package com.onlinestore.main.service.impl;

import com.onlinestore.main.domain.dto.ProductDto;
import com.onlinestore.main.domain.entity.Category;
import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.exception.ProductNotFoundException;
import com.onlinestore.main.mapper.IProductMapper;
import com.onlinestore.main.repository.impl.CategoryRepository;
import com.onlinestore.main.repository.impl.ProductRepository;
import com.onlinestore.main.service.IProductService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@AllArgsConstructor
@Service
public class ProductService implements IProductService {

	private ProductRepository productRepository;

	private CategoryRepository categoryRepository;

	private IProductMapper productMapper;

	@Transactional
	@Override
	public void add(ProductDto productDto) {
		log.info("Starting adding product: " + productDto);

		if (productDto == null) {
			throw new NullPointerException("ProductDto cannot be null");
		}
		Product product = productMapper.mapToProduct(productDto);

		Category category = findCategoryByName(productDto.getCategory());
		if (category == null) {
			category = addCategory(productDto.getCategory());
		}

		product.setCategory(category);

		productRepository.add(product);

		log.info("Finished adding product: " + productDto);
	}

	@Override
	public ProductDto findById(long id) {
		log.info("Starting finding product by id: " + id);

		final ProductDto productDto = productRepository.findById(id)
				.map(product -> productMapper.mapToProductDto(product))
				.orElseThrow(() -> new ProductNotFoundException("Product not was found by id " + id));

		log.info("Finished finding product by id: " + productDto);

		return productDto;
	}

	@Override
	public Page<ProductDto> findAll(Pageable pageable) {
		log.info("Starting finding all products:");
		Page<Product> productsPage = productRepository.findAll(pageable);
		if (productsPage.isEmpty()) {
			throw new ProductNotFoundException("Products were not found");
		}

		log.info("Finished finding all products: " + productsPage);

		return productsPage.map(productMapper::mapToProductDto);
	}

	@Override
	public ProductDto findByName(String name) {
		log.info("Starting finding product by name: " + name);

		final ProductDto productDto = productRepository.findByName(name)
				.map(product -> productMapper.mapToProductDto(product))
				.orElseThrow(() -> new ProductNotFoundException("Product not was found by name " + name));

		log.info("Finished finding  product by id: " + productDto);

		return productDto;
	}

	@Transactional
	@Override
	public void update(ProductDto updateProductDto) {
		log.info("Starting Updating product by id: " + updateProductDto.getId());

		Product product = productRepository.findById(updateProductDto.getId())
				.orElseThrow(() -> new ProductNotFoundException("Product not was found by id " + updateProductDto.getId()));
		Category category = findCategoryByName(updateProductDto.getCategory());

		if (category == null) {
			category = addCategory(updateProductDto.getCategory());

			log.info("Category not found. Added new category successfully: " + category.getName());
		}

		product.setCategory(category);
		productRepository.update(product);

		log.info("Finished product updated successfully: " + product);
	}

	@Transactional
	@Override
	public void deleteByID(long id) {
		log.info("Starting deleting product by id: " + id);

		ProductDto productDto = findById(id);
		if (productDto != null) {
			productRepository.deleteById(id);

			log.info("Finished Product deleted successfully");
		} else {
			throw new ProductNotFoundException("Product not was found by id " + id);
		}
	}

	private Category findCategoryByName(String categoryName) {
		log.info("Finding category by name: " + categoryName);

		return categoryRepository.findByName(categoryName)
				.orElse(null);
	}

	private Category addCategory(String categoryName) {
		log.info("Starting adding new category with name: " + categoryName);

		Category category = new Category();
		category.setName(categoryName);
		categoryRepository.add(category);

		log.info("Finished new category added successfully: " + category);

		return category;
	}
}
