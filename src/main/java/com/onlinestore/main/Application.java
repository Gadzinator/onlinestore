package com.onlinestore.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinestore.main.controller.OrderController;
import com.onlinestore.main.controller.ProductController;
import com.onlinestore.main.controller.UserController;
import com.onlinestore.main.domain.dto.CategoryDto;
import com.onlinestore.main.domain.dto.OrderDto;
import com.onlinestore.main.domain.dto.ProductDto;
import com.onlinestore.main.domain.dto.RegistrationUserDto;
import com.onlinestore.main.domain.entity.Category;
import com.onlinestore.main.domain.entity.OrderStatus;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@ComponentScan
public class Application {

	public static void main(String[] args) throws JsonProcessingException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
		ProductController productController = context.getBean(ProductController.class);

		CategoryDto categoryDtoFirst = new CategoryDto();
		categoryDtoFirst.setId(1);
		categoryDtoFirst.setName(Category.TOY.getValue());

		ObjectMapper objectMapper = new ObjectMapper();
		String s = objectMapper.writeValueAsString(categoryDtoFirst);
		System.out.println(s);
		CategoryDto categoryDto = new CategoryDto();
		System.out.println(categoryDto);

		CategoryDto categoryDtoSecond = new CategoryDto();
		categoryDtoSecond.setId(2);
		categoryDtoSecond.setName(Category.CLOTHES.getValue());

		ProductDto productDtoFirst = new ProductDto();
		productDtoFirst.setId(1L);
		productDtoFirst.setName("Toy name");
		productDtoFirst.setBrand("Toy brand");
		productDtoFirst.setDescription("Toy description");
		productDtoFirst.setCategory(categoryDtoFirst.getName());
		productDtoFirst.setPrice(100);
		productDtoFirst.setCreated("12-10-2023");
		productDtoFirst.setAvailable(true);
		productDtoFirst.setReceived("10-11-2023");
		productController.add(productDtoFirst);

		ProductDto productDtoSecond = new ProductDto();
		productDtoSecond.setId(2);
		productDtoSecond.setName("Clothes name");
		productDtoSecond.setBrand("Clothes brand");
		productDtoSecond.setDescription("Clothes description");
		productDtoSecond.setCategory(categoryDtoSecond.getName());
		productDtoSecond.setPrice(200);
		productDtoSecond.setCreated("11-10-2023");
		productDtoSecond.setAvailable(true);
		productDtoSecond.setReceived("11-12-2023");
		productController.add(productDtoSecond);

		productController.findById(1);
		productController.deleteByID(2);
		productController.updateById(1, productDtoSecond);
		productController.findById(2);

		OrderController orderController = context.getBean(OrderController.class);

		OrderDto orderDtoFirst = new OrderDto();
		orderDtoFirst.setId(1);
		orderDtoFirst.setProducts(List.of(productDtoFirst, productDtoSecond));
		orderDtoFirst.setOrderStatus(OrderStatus.IN_PROGRESS.getValue());
		orderController.add(orderDtoFirst);

		OrderDto orderDtoSecond = new OrderDto();
		orderDtoSecond.setId(2);
		orderDtoSecond.setProducts(List.of(productDtoFirst));
		orderDtoSecond.setOrderStatus(OrderStatus.READY.getValue());
		orderController.add(orderDtoSecond);

		orderController.findById(1);
		orderController.deleteById(2);
		orderController.updateById(1, orderDtoSecond);

		UserController userController = context.getBean(UserController.class);
		RegistrationUserDto registrationUserDtoFirst = new RegistrationUserDto();
		registrationUserDtoFirst.setId(1);
		registrationUserDtoFirst.setName("Name");
		registrationUserDtoFirst.setEmail("name@Gmail.com");
		registrationUserDtoFirst.setPassword("password");
		registrationUserDtoFirst.setConfirmPassword("password");
		userController.createNewUser(registrationUserDtoFirst);

		RegistrationUserDto registrationUserDtoSecond = new RegistrationUserDto();
		registrationUserDtoSecond.setId(2);
		registrationUserDtoSecond.setName("SecondName");
		registrationUserDtoSecond.setEmail("secondName@gmail.com");
		registrationUserDtoSecond.setPassword("secondPassword");
		registrationUserDtoSecond.setConfirmPassword("secondPassword");
		userController.createNewUser(registrationUserDtoSecond);

		userController.findById(1);
		userController.deleteById(2);
		userController.findById(1);
	}
}
