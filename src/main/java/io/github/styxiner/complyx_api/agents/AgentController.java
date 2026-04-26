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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api/agents")
public class AgentController {
	private final AgentService agentService;

	public AgentController(AgentService agentService) {
		super();
		this.agentService = agentService;
	}

//GET ALL
	@GetMapping
	@Operation(summary = "Obtener todos los agentes con filtros y paginaciones")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "pagina de agentes") })
	Page<AgentDTO> getAgents(@ParameterObject AgentFilter agentFilter, @ParameterObject Pageable pageable) {
		return agentService.findAll(agentFilter, pageable);
	}

//GET BY ID	
	@GetMapping("/{agentId}")
	@Operation(summary = "Obtener un agente por su ID")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Agente encontrado"),
			@ApiResponse(responseCode = "404", description = "Agente no encontrado") })
	AgentDTO getAgentById(@Parameter(description = "UUID del agente") @PathVariable UUID agentId) {
		return agentService.findById(agentId);
	}

//ASSIGN GROUP
	@PostMapping("/{agentId}/groups/{groupId}")
	public ResponseEntity<Void> assignGroup(@PathVariable UUID agentId, @PathVariable UUID groupId) {
		agentService.assignGroup(agentId, groupId);
		return ResponseEntity.noContent().build();
	}

//REMOVE GROUP
	@DeleteMapping("/{agentId}/groups/{groupId}")
	public ResponseEntity<Void> removeGroup(@PathVariable UUID agentId, @PathVariable UUID groupId) {
		agentService.removeGroup(agentId, groupId);
		return ResponseEntity.noContent().build();
	}

//DELETE AGENT
	@DeleteMapping("/{agentId}")
	public ResponseEntity<Void> deleteAgent(@PathVariable UUID agentId) {

		agentService.delete(agentId);
		return ResponseEntity.noContent().build();
	}

// ENABLE
	@PatchMapping("/{agentId}/enable")
	public AgentDTO enableAgent(@PathVariable UUID agentId) {

		return agentService.enable(agentId);
	}

	// DISABLE
	@PatchMapping("/{agentId}/disable")
	public AgentDTO disableAgent(@PathVariable UUID agentId) {

		return agentService.disable(agentId);
	}
}
