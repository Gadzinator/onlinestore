package com.onlinestore.main.mapper;

import com.onlinestore.main.domain.dto.OrderResponseDto;
import com.onlinestore.main.domain.dto.ProductDto;
import com.onlinestore.main.domain.entity.Order;
import com.onlinestore.main.domain.entity.User;
import com.onlinestore.main.utils.DateConstant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = IProductMapper.class)
public interface IOrderMapper {

	@Mapping(target = "products", source = "products")
	@Mapping(target = "created", dateFormat = DateConstant.DEFAULT_DATE_PATTERN)
	@Mapping(target = "user", source = "userId", qualifiedByName = "userIdToUser")
	Order mapToOrder(OrderResponseDto orderResponseDto);

	@Mapping(source = "products", target = "products")
	@Mapping(source = "created", target = "created", dateFormat = DateConstant.DEFAULT_DATE_PATTERN)
	@Mapping(source = "user", target = "userId", qualifiedByName = "userToUserId")
	@Mapping(target = "totalPrice", expression = "java(getTotalPrice(orderResponseDto.getProducts()))")
	OrderResponseDto mapToOrderDto(Order order);

	@Named("userIdToUser")
	default User userIdToUser(long userId) {
		User user = new User();
		user.setId(userId);
		return user;
	}

	@Named("userToUserId")
	default long userToUserId(User user) {
		return user.getId();
	}

	default int getTotalPrice(List<ProductDto> dtoList) {
		return dtoList.stream()
				.mapToInt(ProductDto::getPrice)
				.sum();
	}
}
