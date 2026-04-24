package io.github.styxiner.complyx_api.users;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

	private final RoleService roleService;
	
	public RoleController(RoleService service) {
		this.roleService = service;
	}
	
	@GetMapping()
	@Parameters({
		@Parameter(name = "page", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
		@Parameter(name = "size", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "20")),
		@Parameter(name = "sort", in = ParameterIn.QUERY, schema = @Schema(type = "string", example = "rolename,asc"))
	})
	Page<RoleDTO> getAllRoles(@ParameterObject RoleFilter filter, @Parameter(hidden = true) Pageable pageable) {
		return roleService.getAllRoles(filter, pageable);
	}
	
	@GetMapping("{roleId}")
	RoleDTO getRoleById(UUID roleId) {
		return roleService.getRolesById(roleId);
	}
	
	@PostMapping()
	ResponseEntity<RoleDTO> createRole(RoleCreateDTO dto) {
		return ResponseEntity.ok(roleService.create(dto));
	}
	
	@PutMapping("{roleId}")
	RoleDTO updateRole(UUID roleId, RoleUpdateDTO dto) {
		dto.setId(roleId);
		
		return roleService.update(dto);
	}
	
	@DeleteMapping("{roleId}")
	ResponseEntity<Void> deleteRole(UUID roleId) {
		roleService.delete(roleId);
		
		return ResponseEntity.noContent().build();
	}
	
	
}
