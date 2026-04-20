package io.github.styxiner.complyx_api.users;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public class UserController {
	Page<UserDTO> getAllUsers(UserFilter filter, Pageable pageable) {return null;}
	UserDTO getUserById(UUID userId) {return null;}
	ResponseEntity<UserDTO> createUser(UserCreateDTO userCreateDto) {return null;}
	UserDTO updateUser(UUID userId, UserUpdateDTO userUpdateDto) {return null;}
	ResponseEntity<Void> deleteUser(UUID userId) {return null;}
	ResponseEntity<Void> assignRole(UUID userId, UUID roleId) {return null;}
	ResponseEntity<Void> removeRole(UUID userId, UUID roleId) {return null;}
}
