package com.onlinestore.main.mapper;

import com.onlinestore.main.domain.dto.CategoryDto;
import com.onlinestore.main.domain.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ICategoryMapper {

	CategoryDto mapToCategoryDto(Category category);

	Category mapToCategory(CategoryDto categoryDto);
}
