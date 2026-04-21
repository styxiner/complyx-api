package io.github.styxiner.complyx_api.users;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
	UserDTO toDTO(UserEntity user);
	UserEntity toEntity(UserCreateDTO userCreateDto);
}
