package com.onlinestore.main.mapper;

import com.onlinestore.main.domain.dto.OrderDto;
import com.onlinestore.main.domain.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = IProductMapper.class)
public interface IOrderMapper {

	@Mapping(target = "products", source = "products")
	@Mapping(target = "created", dateFormat = "dd-MM-yyyy")
	OrderDto mapToOrder(Order order);

	@Mapping(source = "products", target = "products")
	@Mapping(source = "created", target = "created", dateFormat = "dd-MM-yyyy")
	Order mapToOrderDto(OrderDto orderDto);
}
