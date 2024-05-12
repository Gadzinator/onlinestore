package com.onlinestore.main.service.impl;

import com.onlinestore.main.domain.dto.CategoryDto;
import com.onlinestore.main.domain.dto.ProductDto;
import com.onlinestore.main.domain.entity.Category;
import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.exception.ProductNotFoundException;
import com.onlinestore.main.mapper.IProductMapperImpl;
import com.onlinestore.main.repository.impl.CategoryRepository;
import com.onlinestore.main.repository.impl.ProductRepository;
import com.onlinestore.main.service.impl.config.ServiceTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
public class ProductServiceTest {

	private static final String PRODUCT_NAME = "Toy name";

	private static final long PRODUCT_ID = 1;

	private static final int PAGE_NUMBER = 0;

	private static final int PAGE_SIZE = 10;

	@Mock
	private ProductRepository productRepository;

	@Spy
	private IProductMapperImpl productMapper;

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private ProductService productService;

	@Test
	public void testAdd() {
		Category category = new Category();
		category.setName("TOY");
		ProductDto productDto = createProductDto();
		final Product product = createProduct();

		when(productMapper.mapToProduct(any(ProductDto.class))).thenReturn(product);
		when(categoryRepository.findByName(productDto.getCategory())).thenReturn(Optional.of(category));
		doNothing().when(productRepository).save(product);

		productService.add(productDto);

		verify(productMapper).mapToProduct(productDto);
		verify(categoryRepository).findByName(productDto.getCategory());
		verify(productRepository).save(product);
		assertEquals(product.getId(), productDto.getId());
		assertEquals(product.getName(), productDto.getName());
	}

	@Test
	public void testAddProductWhenNUll() {
		assertThrows(NullPointerException.class, () -> productService.add(null));
	}

	@Test
	public void testFindByIdWhenProductExist() {
		final Product product = createProduct();
		final ProductDto expectedProductDto = createProductDto();

		when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
		when(productMapper.mapToProductDto(product)).thenReturn(expectedProductDto);

		ProductDto actualProductDto = productService.findById(PRODUCT_ID);

		verify(productRepository).findById(PRODUCT_ID);
		verify(productMapper).mapToProductDto(product);
		assertEquals(expectedProductDto, actualProductDto);
	}

	@Test
	public void testFindByIdWhenProductNotExist() {
		when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

		assertThrows(ProductNotFoundException.class, () -> productService.findById(PRODUCT_ID));
	}

	@Test
	public void testFindAllListNotEmpty() {
		final Product firstProduct = createProduct();
		final Product secondProduct = createProduct();
		secondProduct.setId(2);
		final List<Product> products = Arrays.asList(firstProduct, secondProduct);

		final ProductDto firstProductDto = createProductDto();
		final ProductDto secondProductDto = createProductDto();
		secondProductDto.setId(2);

		when(productRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(products));
		when(productMapper.mapToProductDto(firstProduct)).thenReturn(firstProductDto);
		when(productMapper.mapToProductDto(secondProduct)).thenReturn(secondProductDto);

		final Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
		final Page<ProductDto> actualProductDtoList = productService.findAll(pageable);

		verify(productRepository).findAll(any(Pageable.class));
		verify(productMapper).mapToProductDto(firstProduct);
		verify(productMapper).mapToProductDto(secondProduct);


		assertFalse(actualProductDtoList.isEmpty());
		assertEquals(2, actualProductDtoList.getSize());
		assertTrue(actualProductDtoList.getContent().contains(firstProductDto));
		assertTrue(actualProductDtoList.getContent().contains(secondProductDto));
	}

	@Test
	public void testFindAllWhenProductsNotExist() {
		when(productRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

		final Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

		assertThrows(ProductNotFoundException.class, () -> productService.findAll(pageable));
	}

	@Test
	public void findByNameProductExist() {
		final Product product = createProduct();
		final ProductDto productDto = createProductDto();

		when(productRepository.findByName(PRODUCT_NAME)).thenReturn(Optional.of(product));
		when(productMapper.mapToProductDto(product)).thenReturn(productDto);

		final ProductDto actualeProductDto = productService.findByName(PRODUCT_NAME);

		verify(productRepository, times(1)).findByName(PRODUCT_NAME);
		verify(productMapper, times(1)).mapToProductDto(product);

		assertEquals(productDto.getId(), actualeProductDto.getId());
		assertEquals(productDto.getName(), actualeProductDto.getName());
		assertEquals(productDto.getBrand(), actualeProductDto.getBrand());
	}

	@Test
	public void findByNameWhenProductNotExist() {
		when(productRepository.findByName(PRODUCT_NAME)).thenReturn(Optional.empty());

		assertThrows(ProductNotFoundException.class, () -> productService.findByName(PRODUCT_NAME));
	}

	@Test
	public void testUpdateWhenProductFound() {
		final ProductDto productDto = createProductDto();
		Category category = new Category();
		category.setName("TOY");
		final Product product = createProduct();
		product.setName("Product Dto");

		when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
		when(categoryRepository.findByName(productDto.getCategory())).thenReturn(Optional.of(category));
		doNothing().when(productRepository).update(any(Product.class));

		productService.update(productDto);

		final ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

		verify(productRepository).findById(PRODUCT_ID);
		verify(categoryRepository).findByName(productDto.getCategory());
		verify(productRepository).update(productArgumentCaptor.capture());
		final Product savedProduct = productArgumentCaptor.getValue();

		assertEquals("Product Dto", savedProduct.getName());
	}

	@Test
	public void testUpdateWhenProductNotFound() {
		final ProductDto productDto = createProductDto();

		when(productRepository.findById(productDto.getId())).thenReturn(Optional.empty());

		assertThrows(ProductNotFoundException.class, () -> productService.update(productDto));
		verify(productRepository).findById(productDto.getId());
		verify(productRepository, never()).update(any(Product.class));
	}

	@Test
	public void deleteByIDWhenProductExist() {
		final Product product = createProduct();
		final ProductDto expectedProductDto = createProductDto();

		when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
		when(productMapper.mapToProductDto(product)).thenReturn(expectedProductDto);

		productService.deleteByID(PRODUCT_ID);

		verify(productRepository).findById(PRODUCT_ID);
		verify(productMapper).mapToProductDto(product);
	}

	@Test
	public void deleteByIDWhenProductNotExist() {
		when(productRepository.findById(PRODUCT_ID)).thenThrow(ProductNotFoundException.class);

		assertThrows(ProductNotFoundException.class, () -> productService.deleteByID(PRODUCT_ID));
		verify(productRepository, never()).deleteById(PRODUCT_ID);
	}

	private Product createProduct() {
		Category category = new Category();
		category.setName("TOY");
		Product product = new Product();
		product.setId(PRODUCT_ID);
		product.setName(PRODUCT_NAME);
		product.setBrand("Toy brand");
		product.setDescription("Toy description");
		product.setCategory(category);
		product.setPrice(100);
		product.setCreated(LocalDate.now());
		product.setAvailable(true);
		product.setReceived(LocalDate.now().plusDays(3));

		return product;
	}

	private ProductDto createProductDto() {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setId(1);
		categoryDto.setName("TOY");
		ProductDto productDto = new ProductDto();
		productDto.setId(PRODUCT_ID);
		productDto.setName(PRODUCT_NAME);
		productDto.setBrand("Toy brand");
		productDto.setDescription("Toy description");
		productDto.setCategory("TOY");
		productDto.setPrice(100);
		productDto.setCreated("01-11-2024");
		productDto.setAvailable(true);
		productDto.setReceived("01-01-2024");

		return productDto;
	}
}
