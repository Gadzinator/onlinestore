package com.onlinestore.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.onlinestore.main.controller.OrderController;
import com.onlinestore.main.controller.ProductController;
import com.onlinestore.main.domain.dto.CategoryDto;
import com.onlinestore.main.domain.dto.OrderDto;
import com.onlinestore.main.domain.dto.ProductDto;
import com.onlinestore.main.domain.entity.Category;
import com.onlinestore.main.domain.entity.OrderStatus;
import com.onlinestore.main.execute.MyRunnable;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@ComponentScan
public class Application {

	public static void main(String[] args) throws JsonProcessingException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Application.class);

		ProductController productController = context.getBean(ProductController.class);

		final CategoryDto categoryDtoFirst = createCategoryDtoFirst();

		final CategoryDto categoryDtoSecond = createCategoryDtoSecond();

		final ProductDto productDtoFirst = createProductDtoFirst(categoryDtoFirst);

		final ProductDto productDtoSecond = createProductDtoSecond(categoryDtoSecond);

		OrderController orderController = context.getBean(OrderController.class);

		final OrderDto orderDtoFirst = createOrderDtoFirst(List.of(productDtoFirst));

		final OrderDto orderDtoSecond = createOrderDtoSecond(List.of(productDtoFirst));

		Thread thread1 = new Thread(new MyRunnable("Thread 0", orderController::add, orderDtoFirst));
		thread1.start();
		try {
			thread1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Thread thread2 = new Thread(new MyRunnable("Thread 1", orderController::add, orderDtoSecond));
		thread2.start();
		try {
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static CategoryDto createCategoryDtoFirst() {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setId(1);
		categoryDto.setName(Category.TOY.getValue());

		return categoryDto;
	}

	private static CategoryDto createCategoryDtoSecond() {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setId(2);
		categoryDto.setName(Category.ELECTRONICS.getValue());

		return categoryDto;
	}

	private static ProductDto createProductDtoFirst(CategoryDto categoryDto) {
		ProductDto productDto = new ProductDto();
		productDto.setId(3);
		productDto.setName("Toy name");
		productDto.setBrand("Toy brand");
		productDto.setDescription("Toy description");
		productDto.setCategory(categoryDto.getName());
		productDto.setPrice(100);
		productDto.setCreated("12-10-2023");
		productDto.setAvailable(true);
		productDto.setReceived("10-11-2023");

		return productDto;
	}

	private static ProductDto createProductDtoSecond(CategoryDto categoryDto) {
		ProductDto productDto = new ProductDto();
		productDto.setId(15);
		productDto.setName("name");
		productDto.setBrand("brand");
		productDto.setDescription("Electronics description");
		productDto.setCategory(categoryDto.getName());
		productDto.setPrice(200);
		productDto.setCreated("11-10-2023");
		productDto.setAvailable(true);
		productDto.setReceived("11-12-2023");

		return productDto;
	}

	private static OrderDto createOrderDtoFirst(List<ProductDto> productDtoList) {
		OrderDto orderDto = new OrderDto();
		orderDto.setId(16);
		orderDto.setCreated("02-01-2022");
		orderDto.setProducts(productDtoList);
		orderDto.setOrderStatus(OrderStatus.READY.getValue());

		return orderDto;
	}

	private static OrderDto createOrderDtoSecond(List<ProductDto> productDtoList) {
		OrderDto orderDto = new OrderDto();
		orderDto.setId(15);
		orderDto.setCreated("01-01-2001");
		orderDto.setProducts(productDtoList);
		orderDto.setOrderStatus(OrderStatus.NOT_READY.getValue());

		return orderDto;
	}
}
