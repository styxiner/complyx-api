package io.github.styxiner.complyx_api.users;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

	private final RoleService roleService;
	
	public RoleController(RoleService service) {
		this.roleService = service;
	}
	
	@GetMapping()
	Page<RoleDTO> getAllRoles(RoleFilter filter, Pageable pageable) {
		return roleService.getAllRoles(filter, pageable);
	}
	
	@GetMapping("{Id}")
	RoleDTO getRoleById(UUID roleId) {
		return roleService.getRolesById(roleId);
	}
	
	@PostMapping()
	ResponseEntity<RoleDTO> createRole(RoleCreateDTO dto) {
		return ResponseEntity.ok(roleService.create(dto));
	}
	
	@PutMapping("{id}")
	RoleDTO updateRole(UUID roleId, RoleUpdateDTO dto) {
		dto.setId(roleId);
		
		return roleService.update(dto);
	}
	
	@DeleteMapping("{id}")
	ResponseEntity<Void> deleteRole(UUID roleId) {
		roleService.delete(roleId);
		
		return ResponseEntity.noContent().build();
	}
	
	
}
