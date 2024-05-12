package com.onlinestore.main.repository.impl;

import com.onlinestore.main.domain.entity.Category;
import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.repository.impl.config.H2Config;
import com.onlinestore.main.repository.impl.config.LiquibaseConfigTest;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {H2Config.class, LiquibaseConfigTest.class})
@Transactional
public class ProductRepositoryTest {

	@Resource
	private ProductRepository productRepositoryDao;

	private static final String UPDATE_PRODUCT_NAME = "Updated Product Name";

	private static final String PRODUCT_NAME = "Toy name";

	@Test
	public void testAdd() {
		final Product product = createProduct();
		productRepositoryDao.save(product);

		final Optional<Product> optionalProduct = productRepositoryDao.findById(product.getId());

		assertTrue(optionalProduct.isPresent());
		optionalProduct.ifPresent(p -> {
			assertEquals(product.getId(), p.getId());
			assertEquals(product.getName(), p.getName());
		});
	}

	@Test
	public void testFindById() {
		Product product = createProduct();
		productRepositoryDao.save(product);

		final Optional<Product> optionalProduct = productRepositoryDao.findById(product.getId());

		assertTrue(optionalProduct.isPresent());
		optionalProduct.ifPresent(p -> {
			assertEquals(product.getId(), p.getId());
			assertEquals(product.getName(), p.getName());
		});
	}

	@Test
	public void testFindAll() {
		final Product product = createProduct();
		final Product product1 = createProduct();
		productRepositoryDao.save(product);
		productRepositoryDao.save(product1);

		List<Product> productList = productRepositoryDao.findAll();

		assertNotNull(productList);
	}

	@Test
	public void testUpdate() {
		Product product = createProduct();
		productRepositoryDao.save(product);

		product.setName(UPDATE_PRODUCT_NAME);
		productRepositoryDao.update(product);

		final Optional<Product> optionalProduct = productRepositoryDao.findById(product.getId());

		assertTrue(optionalProduct.isPresent());
		optionalProduct.ifPresent(p ->
				assertEquals(UPDATE_PRODUCT_NAME, p.getName()));
	}

	@Test
	public void testFindByName() {
		Product product = createProduct();
		productRepositoryDao.save(product);

		final Optional<Product> optionalProduct = productRepositoryDao.findByName(PRODUCT_NAME);

		assertNotNull(optionalProduct);
		optionalProduct.ifPresent(p ->
				assertEquals(PRODUCT_NAME, p.getName()));
	}

	@Test
	public void testDeleteByID() {
		Product product = createProduct();
		productRepositoryDao.save(product);

		productRepositoryDao.deleteById(product.getId());

		final Optional<Product> optionalProduct = productRepositoryDao.findById(product.getId());
		assertTrue(optionalProduct.isEmpty());
	}

	private Product createProduct() {
		Category category = new Category();
		category.setName("CLOTHES");
		Product product = new Product();
		product.setName(PRODUCT_NAME);
		product.setBrand("Toy brand");
		product.setDescription("Toy description");
		product.setCategory(category);
		product.setPrice(100);
		product.setCreated(LocalDate.parse("2023-10-10"));
		product.setAvailable(true);
		product.setReceived(LocalDate.parse("2023-11-10"));

		return product;
	}
}
