package io.github.styxiner.complyx_api.policies;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

// @RestController indica que esta clase maneja peticiones HTTP y devuelve el resultado directamente en el cuerpo de la respuesta (JSON).
@RestController
//@RequestMapping define la ruta base para todos los endpoints de este controlador.
@RequestMapping("/api/policies")
@Tag(name = "Policies", description = "Gestión de políticas de cumplimiento y normativas")
public class PolicyController {

	private final PolicyService policyService;

	public PolicyController(PolicyService policyService) {
		this.policyService = policyService;
	}
// GET paginado con filtros	
	@GetMapping
    @Operation(summary = "Obtener todas las políticas con filtros y paginación")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Página de políticas obtenida con éxito") })
	public ResponseEntity<Page<PolicySummaryDTO>> getPolicies(@ParameterObject PolicyFilter filter, @ParameterObject Pageable pageable) {

		return ResponseEntity.ok(policyService.getAllPolicies(filter, pageable));
	}
  // GET by ID
    @GetMapping("/{policyId}")
	@Operation(summary = "Obtener detalle completo de una política por su ID")
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Política encontrada"),
        @ApiResponse(responseCode = "404", description = "Política no encontrada") 
    })
	public ResponseEntity<PolicyDetailDTO> getPolicyById(@PathVariable UUID policyId) {

		return ResponseEntity.ok(policyService.getPolicyById(policyId));
	}
 
	// CREATE
    @PostMapping
    @Operation(summary = "Crear una nueva política")
    @ApiResponses({ 
        @ApiResponse(responseCode = "201", description = "Política creada con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos") 
    })
		public ResponseEntity<PolicyDetailDTO> createPolicy(@Valid @RequestBody PolicyCreateDTO dto) {
		PolicyDetailDTO created = policyService.createPolicy(dto);
		URI location = URI.create("/api/policies/" + created.getId()); // se informa la URI del recurso creado.
		return ResponseEntity.created(location).body(created);
    }
	// UPDATE
    @PutMapping("/{policyId}")
    @Operation(summary = "Actualizar una política existente")
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Política actualizada"),
        @ApiResponse(responseCode = "404", description = "Política no encontrada") 
    })
	public ResponseEntity<PolicyDetailDTO> updatePolicy(@Valid @PathVariable UUID policyId, @RequestBody PolicyUpdateDTO dto) {

		return ResponseEntity.ok(policyService.updatePolicy(policyId, dto));
	}

	// DELETE
    @DeleteMapping("/{policyId}")
    @Operation(summary = "Eliminar una política")
    @ApiResponses({ 
        @ApiResponse(responseCode = "204", description = "Politica eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Política no encontrada") 
    })
	public ResponseEntity<Void> deletePolicy(@PathVariable UUID policyId) {

		policyService.deletePolicy(policyId);
		return ResponseEntity.noContent().build();
	}

	// ASSIGN AGENT
    @PostMapping("/{policyId}/agents/{agentId}")
    @Operation(summary = "Asiganar un agente")
    @ApiResponses({ 
        @ApiResponse(responseCode = "204", description = "Agente asignado correctamente"),
        @ApiResponse(responseCode = "404", description = "Politica o Agente no encontrado") 
    })
	public ResponseEntity<Void> assignToAgent(@PathVariable UUID policyId, @PathVariable UUID agentId) {

		policyService.assignToAgent(policyId, agentId);
		return ResponseEntity.noContent().build();
	}

	// UNASSIGN AGENT
    @DeleteMapping("/{policyId}/agents/{agentId}")
    @Operation(summary = "Desasignar agente")
    @ApiResponses({ 
        @ApiResponse(responseCode = "204", description = "Agente asignado correctamente"),
        @ApiResponse(responseCode = "404", description = "Politica o Agente no encontrado") 
    })
	public ResponseEntity<Void> unAssignToAgent(@PathVariable UUID policyId, @PathVariable UUID agentId) {

		policyService.unAssignToAgent(policyId, agentId);
		return ResponseEntity.noContent().build();
	}
// ASSIGN GROUP
	@PostMapping("/{policyId}/groups/{groupId}")
    @Operation(summary = "Asignar un grupo de agentes")
    @ApiResponses({ 
        @ApiResponse(responseCode = "204", description = "Grupo asignado correctamente"),
        @ApiResponse(responseCode = "404", description = "Politica o Grupo no encontrado") 
    })
	
	public ResponseEntity<Void> assignToGroup(@PathVariable UUID policyId, @PathVariable UUID groupId) {

		policyService.assignToGroup(policyId, groupId);
		return ResponseEntity.noContent().build(); 
	}

	// UNASSIGN GROUP
    @DeleteMapping("/{policyId}/groups/{groupId}")
	@Operation(summary = "Desasignar un grupo de agentes")
    @ApiResponses({ 
        @ApiResponse(responseCode = "204", description = "Grupo desasignado correctamente"),
        @ApiResponse(responseCode = "404", description = "Politica o grupo no encontrado") 
    })
	public ResponseEntity<Void> unAssignToGroup(@PathVariable UUID policyId, @PathVariable UUID groupId) {

		policyService.unAssignToGroup(policyId, groupId);
		return ResponseEntity.noContent().build();
	}

	// GET BY AGENT
    @Operation(summary = "Conseguir politica por su agente")
    @ApiResponses({ 
        @ApiResponse(responseCode = "204", description = "Lista de politicas obtenida correctamente"),
        @ApiResponse(responseCode = "404", description = "Agente no encontrado") 
    })
	@GetMapping("/agent/{agentId}")
	public ResponseEntity<List<PolicySummaryDTO>> getPoliciesByAgent(@PathVariable UUID agentId) {

		return ResponseEntity.ok(policyService.getPoliciesByAgent(agentId));
	}
}