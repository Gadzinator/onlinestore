package com.onlinestore.main.repository.impl;

import com.onlinestore.main.domain.entity.Category;
import com.onlinestore.main.domain.entity.Order;
import com.onlinestore.main.domain.entity.OrderStatus;
import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.repository.impl.config.H2Config;
import com.onlinestore.main.repository.impl.config.LiquibaseConfigTest;
import jakarta.annotation.Resource;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {H2Config.class, LiquibaseConfigTest.class})
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrderRepositoryTest {

	@Resource
	private OrderRepository orderRepository;

	@Resource
	private ProductRepository productRepository;

	@Test
	public void testAdd() {
		final Product product = createProduct();
		final Order order = createOrder(product);
		orderRepository.add(order);

		final Optional<Order> optionalOrder = orderRepository.findById(order.getId());

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
		final Order order = createOrder(product);

		orderRepository.add(order);

		Optional<Order> optionalOrder = orderRepository.findById(order.getId());

		assertTrue(optionalOrder.isPresent());
		Order retrievedOrder = optionalOrder.get();
		assertEquals(order.getId(), retrievedOrder.getId());
		assertEquals(order.getCreated(), retrievedOrder.getCreated());
		assertEquals(order.getOrderStatus(), retrievedOrder.getOrderStatus());
	}

	@Test
	public void testFindAll() {
		final Product product = createProduct();
		final Order order1 = createOrder(product);

		Order order2 = new Order();
		order2.setCreated(LocalDate.now());
		order2.setOrderStatus(OrderStatus.READY);
		orderRepository.add(order1);
		orderRepository.add(order2);

		List<Order> allOrders = orderRepository.findAll();

		assertNotNull(allOrders);
		assertEquals(2, allOrders.size());
		assertTrue(allOrders.contains(order1));
		assertTrue(allOrders.contains(order2));
	}

	@Test
	public void testUpdate() {
		Product product = createProduct();
		productRepository.add(product);

		final Order order = createOrder(product);
		orderRepository.add(order);

		order.setOrderStatus(OrderStatus.IN_PROGRESS);

		orderRepository.update(order);

		Order retrievedOrder = orderRepository.findById(order.getId()).orElse(null);
		assertNotNull(retrievedOrder);
		assertEquals(order.getOrderStatus(), retrievedOrder.getOrderStatus());
	}

	@Test
	public void testDeleteById() {
		Product product = createProduct();
		productRepository.add(product);
		long productId = product.getId();

		productRepository.deleteById(productId);

		Optional<Product> deletedProductOptional = productRepository.findById(productId);
		assertFalse(deletedProductOptional.isPresent(), "Сущность не должна быть найдена после удаления");
	}

	@Test
	public void testFindProductsOrderId() {
		final Product product = createProduct();
		final Order order = createOrder(product);
		final List<Product> productList = orderRepository.findProductsOrderId(order.getId());

		assertTrue(productList.isEmpty());
	}

	private Order createOrder(Product product) {
		List<Product> products = new ArrayList<>();
		products.add(product);
		Order order = new Order();
		order.setProducts(products);
		order.setCreated(LocalDate.parse("2023-11-11"));
		order.setOrderStatus(OrderStatus.NOT_READY);

		return order;
	}

	private Product createProduct() {
		Category category = new Category();
		category.setName("CLOTHES");
		Product product = new Product();
		product.setName("Toy name");
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
