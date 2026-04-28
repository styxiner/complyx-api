package io.github.styxiner.complyx_api.agents;

import java.net.URI;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

/*
 * Controlador REST para gestionar grupos de agentes. Expone los endpoints HTTP.
 */
@RestController
@RequestMapping("api/groups")
public class AgentGroupController {

	private final AgentGroupService groupService;

	public AgentGroupController(AgentGroupService groupService) {
		this.groupService = groupService;
	}

	// Obtiene todos los grupos paginados mediante GET /api/groups
	@GetMapping
	@Operation(summary = "Obtener todos los grupos de agentes con filtros y paginacion")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Pagina de grupos") })
	public Page<AgentGroupDTO> getGroups(@ParameterObject AgentGroupFilter filter, @ParameterObject Pageable pageable) {
		return groupService.findAll(filter,pageable);
	}

	// Obtiene un grupo por ID
	@GetMapping("/{groupId}")
	@Operation(summary = "Obtener grupo por ID")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Grupo encontrado"),
			@ApiResponse(responseCode = "404", description = "Grupo no encontrado") })
	public AgentGroupDTO getGroupById(@PathVariable UUID groupId) {
		return groupService.findById(groupId);
	}

	// Crea un nuevo grupo
	@PostMapping
	@Operation(summary = "Crear un nuevo grupo")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Grupo creado"),
			@ApiResponse(responseCode = "400", description = "Datos inválidos"),
			@ApiResponse(responseCode = "409", description = "Grupo duplicado") })
	// convierte JSON->DTO y valida @NotBlank
	public ResponseEntity<AgentGroupDTO> createGroup(@Valid @RequestBody AgentGroupCreateDTO dto) {
		AgentGroupDTO created = groupService.create(dto);
		URI location = URI.create("/api/groups/" + created.getId()); //URI-Clase estándar para identificar recursos en REST
		// Devuelve 201 Created e incluye en el header la URL del recurso creado (Location)
		return ResponseEntity.created(location).body(created);
		
	}

	// Elimina un grupo
	@DeleteMapping("/{groupId}")
	@Operation(summary = "Eliminar grupo")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Grupo eliminado"),
			@ApiResponse(responseCode = "404", description = "Grupo no encontrado") })
	public ResponseEntity<Void> deleteGroup(@PathVariable UUID groupId) {
		groupService.delete(groupId);
		return ResponseEntity.noContent().build();
	}

	// Actualiza parcialmente un grupo
	@PatchMapping("/{groupId}")
	@Operation(summary = "Actualizar grupo parcialmente")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Grupo actualizado"),
			@ApiResponse(responseCode = "404", description = "Grupo no encontrado"),
			@ApiResponse(responseCode = "409", description = "Nombre duplicado") })
	public AgentGroupDTO updateGroup(@PathVariable UUID groupId, @RequestBody AgentGroupUpdateDTO dto) {
		return groupService.update(groupId, dto);
	}
}
