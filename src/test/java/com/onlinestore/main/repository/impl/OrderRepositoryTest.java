package com.onlinestore.main.repository.impl;

import com.onlinestore.main.domain.entity.Category;
import com.onlinestore.main.domain.entity.Order;
import com.onlinestore.main.domain.entity.OrderStatus;
import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.repository.impl.config.H2Config;
import com.onlinestore.main.repository.impl.config.LiquibaseConfigTest;
import jakarta.annotation.Resource;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {H2Config.class, LiquibaseConfigTest.class})
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrderRepositoryTest {

	@Resource
	private OrderRepository orderRepositoryDao;

	@Resource
	private ProductRepository productRepositoryDao;

	@After
	public void tirDown() {

	}

	@Test
	public void testAdd() {
		final Product product = createProduct();
		final Order order = createorder(product);
		orderRepositoryDao.add(order);

		final Optional<Order> optionalOrder = orderRepositoryDao.findById(order.getId());

		assertTrue(optionalOrder.isPresent());
		Order retrievedOrder = optionalOrder.get();
		assertEquals(order.getId(), retrievedOrder.getId());
		assertEquals(order.getCreated(), retrievedOrder.getCreated());
		assertEquals(order.getOrderStatus(), retrievedOrder.getOrderStatus());

		assertEquals(order.getProducts().size(), retrievedOrder.getProducts().size());

		for (int i = 0; i < order.getProducts().size(); i++) {
			Product originalProduct = order.getProducts().get(i);
			Product retrievedProduct = retrievedOrder.getProducts().get(i);
			assertEquals(originalProduct.getId(), retrievedProduct.getId());
		}
	}

	@Test
	public void testFindById() {
		final Product product = createProduct();
		final Order order = createorder(product);

		orderRepositoryDao.add(order);

		Optional<Order> optionalOrder = orderRepositoryDao.findById(order.getId());

		assertTrue(optionalOrder.isPresent());
		Order retrievedOrder = optionalOrder.get();
		assertEquals(order.getId(), retrievedOrder.getId());
		assertEquals(order.getCreated(), retrievedOrder.getCreated());
		assertEquals(order.getOrderStatus(), retrievedOrder.getOrderStatus());
	}

	@Test
	public void testFindAll() {
		final Product product = createProduct();
		final Order order1 = createorder(product);

		Order order2 = new Order();
		order2.setCreated(LocalDate.now());
		order2.setOrderStatus(OrderStatus.READY);
		orderRepositoryDao.add(order1);
		orderRepositoryDao.add(order2);

		List<Order> allOrders = orderRepositoryDao.findAll();

		assertNotNull(allOrders);
		assertEquals(2, allOrders.size());
		assertTrue(allOrders.contains(order1));
		assertTrue(allOrders.contains(order2));
	}

	@Test
	public void testUpdate() {
		Product product = createProduct();
		productRepositoryDao.add(product);

		final Order order = createorder(product);
		orderRepositoryDao.add(order);

		order.setOrderStatus(OrderStatus.IN_PROGRESS);

		orderRepositoryDao.update(order);

		Order retrievedOrder = orderRepositoryDao.findById(order.getId()).orElse(null);
		assertNotNull(retrievedOrder);
		assertEquals(order.getOrderStatus(), retrievedOrder.getOrderStatus());
	}

	@Test
	public void testDeleteById() {
		Product product = createProduct();
		productRepositoryDao.add(product);
		long productId = product.getId();

		productRepositoryDao.deleteById(productId);

		Optional<Product> deletedProductOptional = productRepositoryDao.findById(productId);
		assertFalse(deletedProductOptional.isPresent(), "Сущность не должна быть найдена после удаления");
	}

	@Test
	public void testFindProductsOrderId() {
		final Product product = createProduct();
		final Order order = createorder(product);
		final List<Product> productList = orderRepositoryDao.findProductsOrderId(order.getId());

		assertTrue(productList.isEmpty());
	}

	private Order createorder(Product product) {
		List<Product> products = new ArrayList<>();
		products.add(product);
		Order order = new Order();
		order.setProducts(products);
		order.setCreated(LocalDate.parse("2023-11-11"));
		order.setOrderStatus(OrderStatus.NOT_READY);

		return order;
	}

	private Product createProduct() {
		Product product = new Product();
		product.setName("Toy name");
		product.setBrand("Toy brand");
		product.setDescription("Toy description");
		product.setCategory(Category.CLOTHES);
		product.setPrice(100);
		product.setCreated(LocalDate.parse("2023-10-10"));
		product.setAvailable(true);
		product.setReceived(LocalDate.parse("2023-11-10"));

		return product;
	}
}
