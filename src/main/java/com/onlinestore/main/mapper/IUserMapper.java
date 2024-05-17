package com.onlinestore.main.mapper;

import com.onlinestore.main.domain.dto.UserDto;
import com.onlinestore.main.domain.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserMapper {

	UserDto mapToUserDto(User user);

	User mapToUser(UserDto userDto);
}
