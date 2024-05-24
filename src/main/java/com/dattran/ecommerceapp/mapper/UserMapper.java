package com.dattran.ecommerceapp.mapper;

import com.dattran.ecommerceapp.dto.request.UserRequest;
import com.dattran.ecommerceapp.dto.response.UserResponse;
import com.dattran.ecommerceapp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", ignore = true)
    User toUser(UserRequest request);
    @Mapping(target = "role", source = "user.role.name")
    UserResponse toUserResponse(User user);
}
