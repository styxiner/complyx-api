package io.github.styxiner.complyx_api.agents;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api/agents")
public class AgentController {

	private final AgentService agentService;

	public AgentController(AgentService agentService) {
		this.agentService = agentService;
	}

	// Lista los agentes aplicando filtros y paginacion.
	@GetMapping
	@Operation(summary = "Obtener todos los agentes con filtros y paginacion")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Pagina de agentes")
	})
	public ResponseEntity<Page<AgentDTO>> getAgents(@ParameterObject AgentFilter agentFilter,
			@ParameterObject Pageable pageable) {
		return ResponseEntity.ok(agentService.findAll(agentFilter, pageable));
	}

	@GetMapping("/{agentId}")
	@Operation(summary = "Obtener un agente por su ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Agente encontrado"),
			@ApiResponse(responseCode = "404", description = "Agente no encontrado")
	})
	public ResponseEntity<AgentDTO> getAgentById(@PathVariable UUID agentId) {
		return ResponseEntity.ok(agentService.findById(agentId));
	}

	// Asigna un grupo existente a un agente existente.
	@PostMapping("/{agentId}/groups/{groupId}")
	public ResponseEntity<Void> assignGroup(@PathVariable UUID agentId, @PathVariable UUID groupId) {
		agentService.assignGroup(agentId, groupId);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{agentId}/groups/{groupId}")
	public ResponseEntity<Void> removeGroup(@PathVariable UUID agentId, @PathVariable UUID groupId) {
		agentService.removeGroup(agentId, groupId);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{agentId}")
	public ResponseEntity<Void> deleteAgent(@PathVariable UUID agentId) {
		agentService.delete(agentId);
		return ResponseEntity.noContent().build();
	}

	// Activa el agente sin modificar el resto de sus datos.
	@PatchMapping("/{agentId}/enable")
	public ResponseEntity<AgentDTO> enableAgent(@PathVariable UUID agentId) {
		return ResponseEntity.ok(agentService.enable(agentId));
	}

	@PatchMapping("/{agentId}/disable")
	public ResponseEntity<AgentDTO> disableAgent(@PathVariable UUID agentId) {
		return ResponseEntity.ok(agentService.disable(agentId));
	}
}