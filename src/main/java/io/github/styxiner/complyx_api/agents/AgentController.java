package io.github.styxiner.complyx_api.agents;

import java.net.URI;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.github.styxiner.complyx_api.policies.PolicyComplianceDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/agents")
public class AgentController {

	private final AgentService agentService;

	public AgentController(AgentService agentService) {
		this.agentService = agentService;
	}

	// Lista los agentes aplicando filtros y paginacion.
	@PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
	@GetMapping
	@Operation(summary = "Obtener todos los agentes con filtros y paginacion")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Pagina de agentes") })
	public ResponseEntity<Page<AgentDTO>> getAgents(@ParameterObject AgentFilter agentFilter,
			@ParameterObject Pageable pageable) {
		return ResponseEntity.ok(agentService.findAll(agentFilter, pageable));
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
	@GetMapping("/{agentId}")
	@Operation(summary = "Obtener un agente por su ID")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Agente encontrado"),
			@ApiResponse(responseCode = "404", description = "Agente no encontrado") })
	public ResponseEntity<AgentDTO> getAgentById(@PathVariable UUID agentId) {
		return ResponseEntity.ok(agentService.findById(agentId));
	}

	// Registra un nuevo agente
	@PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
	@PostMapping("/register")
	@Operation(summary = "Registrar un nuevo agente")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Agente registrado"),
			@ApiResponse(responseCode = "400", description = "Datos invalidos"),
			@ApiResponse(responseCode = "409", description = "Agente duplicado por IP") })
	public ResponseEntity<AgentDTO> registerAgent(@Valid @RequestBody AgentRegisterDTO dto) {
		AgentDTO created = agentService.register(dto);
		URI location = URI.create("/api/agents/" + created.getId());
		return ResponseEntity.created(location).body(created);
	}

	// Asigna un grupo existente a un agente existente.
	@PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
	@PostMapping("/{agentId}/groups/{groupId}")
	@Operation(summary = "Asignar un grupo a un agente")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Grupo asignado al agente"),
			@ApiResponse(responseCode = "404", description = "Agente o grupo no encontrado") })
	public ResponseEntity<Void> assignGroup(@PathVariable UUID agentId, @PathVariable UUID groupId) {
		agentService.assignGroup(agentId, groupId);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
	@DeleteMapping("/{agentId}/groups/{groupId}")
	@Operation(summary = "Quitar un grupo de un agente")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Grupo eliminado del agente"),
			@ApiResponse(responseCode = "404", description = "Agente o grupo no encontrado") })
	public ResponseEntity<Void> removeGroup(@PathVariable UUID agentId, @PathVariable UUID groupId) {
		agentService.removeGroup(agentId, groupId);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
	@DeleteMapping("/{agentId}")
	@Operation(summary = "Eliminar un agente")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Agente eliminado"),
			@ApiResponse(responseCode = "404", description = "Agente no encontrado") })
	public ResponseEntity<Void> deleteAgent(@PathVariable UUID agentId) {
		agentService.delete(agentId);
		return ResponseEntity.noContent().build();
	}

	// Activa el agente sin modificar el resto de sus datos.
	@PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
	@PatchMapping("/{agentId}/enable")
	@Operation(summary = "Activar un agente")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Agente activado"),
			@ApiResponse(responseCode = "404", description = "Agente no encontrado") })
	public ResponseEntity<AgentDTO> enableAgent(@PathVariable UUID agentId) {
		return ResponseEntity.ok(agentService.enable(agentId));
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
	@PatchMapping("/{agentId}/disable")
	@Operation(summary = "Desactivar un agente")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Agente desactivado"),
			@ApiResponse(responseCode = "404", description = "Agente no encontrado") })
	public ResponseEntity<AgentDTO> disableAgent(@PathVariable UUID agentId) {
		return ResponseEntity.ok(agentService.disable(agentId));
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'TECNICO', 'AUDITOR')")
	@GetMapping("/{agentId}/policies/{policyId}/results")
	@Operation(summary = "Obtener los resultados de cumplimiento de una política para un agente")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Resultados obtenidos correctamente"),
			@ApiResponse(responseCode = "404", description = "Agente o política no encontrados") })
	public ResponseEntity<PolicyComplianceDTO> getPolicyResults(@PathVariable UUID agentId,
			@PathVariable UUID policyId) {
		return ResponseEntity.ok(agentService.getPolicyResults(agentId, policyId));
	}
}