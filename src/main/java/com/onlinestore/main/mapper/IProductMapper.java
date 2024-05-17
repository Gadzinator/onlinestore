package com.onlinestore.main.mapper;

import com.onlinestore.main.domain.dto.ProductDto;
import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.utils.DateConstant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ICategoryMapper.class)
public interface IProductMapper {

	@Mapping(target = "created", dateFormat = DateConstant.DEFAULT_DATE_PATTERN)
	@Mapping(target = "received", dateFormat = DateConstant.DEFAULT_DATE_PATTERN)
	@Mapping(source = "category.name", target = "category")
	ProductDto mapToProductDto(Product product);

	@Mapping(source = "created", target = "created", dateFormat = DateConstant.DEFAULT_DATE_PATTERN)
	@Mapping(source = "received", target = "received", dateFormat = DateConstant.DEFAULT_DATE_PATTERN)
	@Mapping(source = "category", target = "category.name")
	Product mapToProduct(ProductDto productDto);
}
