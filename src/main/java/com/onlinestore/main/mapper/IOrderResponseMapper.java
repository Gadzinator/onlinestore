package com.onlinestore.main.mapper;

import com.onlinestore.main.domain.dto.OrderRequestDto;
import com.onlinestore.main.domain.entity.Order;
import com.onlinestore.main.domain.entity.OrderStatus;
import com.onlinestore.main.domain.entity.Product;
import com.onlinestore.main.utils.DateConstant;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ICategoryMapper.class, IProductMapper.class})
public interface IOrderResponseMapper {

	@Mapping(source = "productIds", target = "products")
	@Mapping(target = "created", dateFormat = DateConstant.DEFAULT_DATE_PATTERN)
	@Mapping(source = "orderStatus", target = "orderStatus", qualifiedByName = "mapOrderStatusToString")
	Order mapToOrder(OrderRequestDto orderRequestDto);

	@InheritInverseConfiguration
	@Mapping(source = "products", target = "productIds")
	@Mapping(source = "created", target = "created", dateFormat = DateConstant.DEFAULT_DATE_PATTERN)
	@Mapping(source = "orderStatus", target = "orderStatus", qualifiedByName = "mapStringToOrderStatus")
	OrderRequestDto mapToOrderRequestDto(Order order);

	default List<Product> mapProductIdsToProducts(List<Long> productIds) {
		if (productIds == null) {
			return null;
		}
		return productIds.stream()
				.map(productId -> {
					Product product = new Product();
					product.setId(productId);
					return product;
				})
				.collect(Collectors.toList());
	}

	default List<Long> mapProductsToProductIds(List<Product> products) {
		if (products == null) {
			return null;
		}
		return products.stream()
				.map(Product::getId)
				.collect(Collectors.toList());
	}

	@Named("mapOrderStatusToString")
	default String mapOrderStatusToString(OrderStatus orderStatus) {
		return orderStatus != null ? orderStatus.name() : null;
	}

	@Named("mapStringToOrderStatus")
	default OrderStatus mapStringToOrderStatus(String orderStatus) {
		return orderStatus != null ? OrderStatus.valueOf(orderStatus) : null;
	}
}
