package com.onlinestore.main.mapper;

import com.onlinestore.main.domain.dto.UserDto;
import com.onlinestore.main.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IUserMapper {

	@Mapping(target = "id", source = "id")
	UserDto mapToUserDto(User user);

	@Mapping(source = "id", target = "id")
	User mapToUser(UserDto userDto);
}
