package com.onlinestore.main.repository.impl;

import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.repository.IProductRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository implements IProductRepository {

	private final List<Product> products = new ArrayList<>();

	@Override
	public void add(Product product) {
		products.add(product);
	}

	@Override
	public Optional<Product> findById(long id) {
		return products.stream()
				.filter(product -> product.getId() == id)
				.findFirst();
	}

	@Override
	public void updateById(long id, Product updateProduct) {
		for (int i = 0; i < products.size(); i++) {
			Product product = products.get(i);
			if (product.getId() == id) {
				products.set(i, updateProduct);
			}
		}
	}

	@Override
	public void delete(long id) {
		products.removeIf(product -> product.getId() == id);
	}
}
