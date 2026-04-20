package io.github.styxiner.complyx_api.users;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class UserService {
	Page<UserDTO> findAll(UserFilter filter, Pageable pageable) {return null;}
	UserDTO findById(UUID userId) {return null;}
	UserDTO create(UserCreateDTO userCreateDto) {return null;}
	UserDTO update(UserUpdateDTO userUpdateDto) {return null;}
	void delete(UUID userId) {}
	void assignRole(UUID userId, UUID roleId) {}
	boolean existsByUserName(String name) {return false;}
}
