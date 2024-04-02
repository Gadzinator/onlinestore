package com.onlinestore.main.mapper;

import com.onlinestore.main.domain.dto.ProductDto;
import com.onlinestore.main.domain.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IProductMapper {

	@Mapping(target = "created", dateFormat = "dd-MM-yyyy")
	@Mapping(target = "received", dateFormat = "dd-MM-yyyy")
	ProductDto mapToProductDto(Product product);

	@Mapping(source = "created", target = "created", dateFormat = "dd-MM-yyyy")
	@Mapping(source = "received", target = "received", dateFormat = "dd-MM-yyyy")
	Product mapToProduct(ProductDto productDto);
}
