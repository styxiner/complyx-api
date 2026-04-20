package io.github.styxiner.complyx_api.users;

public interface UserMapper {
	UserDTO toDTO(UserEntity user);
	UserEntity toEntity(UserCreateDTO userCreateDto);
}
