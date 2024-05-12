package com.onlinestore.main.repository.impl;

import com.onlinestore.main.domain.entity.Category;
import com.onlinestore.main.repository.impl.config.H2Config;
import com.onlinestore.main.repository.impl.config.LiquibaseConfigTest;
import jakarta.annotation.Resource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {H2Config.class, LiquibaseConfigTest.class})
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CategoryRepositoryTest {

	private static final long CATEGORY_ID = 1;

	private static final String CATEGORY_NAME = "Clothes";

	@Resource
	private CategoryRepository categoryRepository;

	@Test
	public void testAdd() {
		// given
		final Category category = createCategory();

		// when
		categoryRepository.add(category);

		// then
		final Category retrievedCategory = categoryRepository.findById(CATEGORY_ID).get();

		assertEquals(category.getName(), retrievedCategory.getName());
	}

	@Test
	public void findAll() {
	}

	@Test
	public void update() {
	}

	@Test
	public void deleteById() {
	}

	@Test
	public void findById() {
	}

	@Test
	public void testFindByName() {
		final Category category = createCategory();

		final Optional<Category> optionalCategory = categoryRepository.findById(CATEGORY_ID);

		Assert.assertNotNull(optionalCategory);
		optionalCategory.ifPresent(p -> Assert.assertEquals(category.getName(), p.getName()));
	}

	private Category createCategory() {
		Category category = new Category();
		category.setName(CATEGORY_NAME);

		return category;
	}
}