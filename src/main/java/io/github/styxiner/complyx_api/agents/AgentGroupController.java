package io.github.styxiner.complyx_api.agents;

import java.net.URI;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/groups")
public class AgentGroupController {

	private final AgentGroupService groupService;

	public AgentGroupController(AgentGroupService groupService) {
		this.groupService = groupService;
	}

	@PreAuthorize("hasRole('ADMIN', 'TECNICO')")
	@GetMapping
	@Operation(summary = "Obtener todos los grupos de agentes con filtros y paginacion")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Pagina de grupos")
	})
	public ResponseEntity<Page<AgentGroupDTO>> getGroups(@ParameterObject AgentGroupFilter filter, @ParameterObject Pageable pageable) {
		return ResponseEntity.ok(groupService.findAll(filter, pageable));
	}

	@PreAuthorize("hasRole('ADMIN', 'TECNICO')")
	@GetMapping("/{groupId}")
	@Operation(summary = "Obtener grupo por ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Grupo encontrado"),
			@ApiResponse(responseCode = "404", description = "Grupo no encontrado")
	})
	public ResponseEntity<AgentGroupDTO> getGroupById(@PathVariable UUID groupId) {
		return ResponseEntity.ok(groupService.findById(groupId));
	}

	// Crea un grupo nuevo y devuelve la URL del recurso creado.
	@PreAuthorize("hasRole('ADMIN', 'TECNICO')")
	@PostMapping
	@Operation(summary = "Crear un nuevo grupo")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Grupo creado"),
			@ApiResponse(responseCode = "400", description = "Datos invalidos"),
			@ApiResponse(responseCode = "409", description = "Grupo duplicado")
	})
	public ResponseEntity<AgentGroupDTO> createGroup(@Valid @RequestBody AgentGroupCreateDTO dto) {
		AgentGroupDTO created = groupService.create(dto);
		URI location = URI.create("/api/groups/" + created.getId());
		return ResponseEntity.created(location).body(created);
	}

	@PreAuthorize("hasRole('ADMIN', 'TECNICO')")
	@DeleteMapping("/{groupId}")
	@Operation(summary = "Eliminar grupo")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Grupo eliminado"),
			@ApiResponse(responseCode = "404", description = "Grupo no encontrado")
	})
	public ResponseEntity<Void> deleteGroup(@PathVariable UUID groupId) {
		groupService.delete(groupId);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasRole('ADMIN', 'TECNICO')")
	@PatchMapping("/{groupId}")
	@Operation(summary = "Actualizar grupo parcialmente")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Grupo actualizado"),
			@ApiResponse(responseCode = "404", description = "Grupo no encontrado"),
			@ApiResponse(responseCode = "409", description = "Nombre duplicado")
	})
	public ResponseEntity<AgentGroupDTO> updateGroup(@PathVariable UUID groupId, @RequestBody AgentGroupUpdateDTO dto) {
		return ResponseEntity.ok(groupService.update(groupId, dto));
	}
}