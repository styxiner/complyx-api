package io.github.styxiner.complyx_api.agents;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/*
 * Controlador REST que actúa como punto de entrada para la gestión de agentes. 
 * Expone endpoints para operaciones CRUD, filtrado dinámico, paginación, gestión de estados...
 * Se usa ResponseEntity para tener control sobre la respuesta HTTP(status, headers, body) y mantener consistencia
 */
@RestController
@RequestMapping("api/agents")
public class AgentController {
	private final AgentService agentService;

	public AgentController(AgentService agentService) {
		super();
		this.agentService = agentService;
	}

//GET ALL - Obtiene todos los agentes paginados
	@GetMapping
	@Operation(summary = "Obtener todos los agentes con filtros y paginaciones")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "pagina de agentes") })
	public ResponseEntity<Page<AgentDTO>> getAgents(@ParameterObject AgentFilter agentFilter,
			@ParameterObject Pageable pageable) {
		return ResponseEntity.ok(agentService.findAll(agentFilter, pageable));
	}

//GET BY ID	-Obtiene todos los agentes por ID
	@GetMapping("/{agentId}")
	@Operation(summary = "Obtener un agente por su ID")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Agente encontrado"),
			@ApiResponse(responseCode = "404", description = "Agente no encontrado") })
	public ResponseEntity<AgentDTO> getAgentById(@PathVariable UUID agentId) {
		return ResponseEntity.ok(agentService.findById(agentId));
	}

//ASSIGN GROUP-Establece una relación en la BD para vincular un agente con un grupo determinado.
	@PostMapping("/{agentId}/groups/{groupId}")
	public ResponseEntity<Void> assignGroup(@PathVariable UUID agentId, @PathVariable UUID groupId) {
		agentService.assignGroup(agentId, groupId);
		return ResponseEntity.noContent().build();
	}

//REMOVE GROUP-Elimina la asociación existente entre un agente y un grupo específico sin borrar las entidades. 
	@DeleteMapping("/{agentId}/groups/{groupId}")
	public ResponseEntity<Void> removeGroup(@PathVariable UUID agentId, @PathVariable UUID groupId) {
		agentService.removeGroup(agentId, groupId);
		return ResponseEntity.noContent().build();
	}

//DELETE AGENT-Realiza la eliminación física del registro de un agente en el sistema utilizando su ID.
	@DeleteMapping("/{agentId}")
	public ResponseEntity<Void> deleteAgent(@PathVariable UUID agentId) {
		agentService.delete(agentId);
		return ResponseEntity.noContent().build();
	}

// ENABLE-Actualiza el estado del agente habilitandolo para permitir su funcionamiento.
	@PatchMapping("/{agentId}/enable")
	public ResponseEntity<AgentDTO> enableAgent(@PathVariable UUID agentId) {
		return ResponseEntity.ok(agentService.enable(agentId));
	}

	// DISABLE-Cambia el estado del agente a deshabilitado para suspender
	// temporalmente su actividad.
	@PatchMapping("/{agentId}/disable")
	public ResponseEntity<AgentDTO> disableAgent(@PathVariable UUID agentId) {
		return ResponseEntity.ok(agentService.disable(agentId));
	}
}
