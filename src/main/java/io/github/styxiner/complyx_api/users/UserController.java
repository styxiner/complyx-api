package io.github.styxiner.complyx_api.users;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService service) {
		this.userService = service;
	}
	
	// En swagger no funciona porque swagger interpreta sort como un array JSON en vez de una query de parametros repetibles
	// Al final se soluciona cambiando el formato de parámetros para que no tenga que ser array
	/*
	@GetMapping()
	Page<UserDTO> getAllUsers(@ParameterObject UserFilter filter, Pageable pageable) {
		return userService.getAllUsers(filter, pageable);
	}*/
	@GetMapping()
	@Parameters({
	    @Parameter(name = "page", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
	    @Parameter(name = "size", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "20")),
	    @Parameter(name = "sort", in = ParameterIn.QUERY, description = "field,direction — e.g. username,asc",
	               schema = @Schema(type = "string"), example = "username,asc")
	})
	Page<UserDTO> getAllUsers(@ParameterObject UserFilter filter, @Parameter(hidden = true) Pageable pageable) {
	    return userService.getAllUsers(filter, pageable);
	}
	
	@GetMapping("{userId}")
	UserDTO getUserById(@PathVariable UUID userId) {

    return userService.getUserById(userId);
	}
	
	@PostMapping()
	ResponseEntity<UserDTO> createUser(@RequestBody UserCreateDTO userCreateDto) {
		return ResponseEntity.ok(userService.create(userCreateDto));
	}
	
	@PutMapping("{id}")
	UserDTO updateUser(@PathVariable UUID userId, @RequestBody UserUpdateDTO userUpdateDto) {
		userUpdateDto.setId(userId);
		
		return userService.update(userUpdateDto);
	}
	
	@DeleteMapping("{userId}")
	ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
		userService.delete(userId);
		
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("{userId}/roles/{roleId}")
	ResponseEntity<Void> assignRole(@PathVariable UUID userId, @PathVariable UUID roleId) {
		userService.assignRole(userId, roleId);
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("{userId}/roles/{roleId}")
	ResponseEntity<Void> removeRole(@PathVariable UUID userId, @PathVariable UUID roleId) {
		userService.unassignRole(userId, roleId);
		
		return ResponseEntity.noContent().build();
	}
}
