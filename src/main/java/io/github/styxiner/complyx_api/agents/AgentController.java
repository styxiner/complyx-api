package io.github.styxiner.complyx_api.agents;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api/agents")
public class AgentController {
	
	@Operation(summary = "Obtener todos los agentes con filtros y paginaciones")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "pagina de agentes")
	})
	Page<AgentDTO> getAgents(@ParameterObject AgentFilter agentFilter, @ParameterObject Pageable pageable) {return null;}
	
    @Operation(summary = "Obtener un agente por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Agente encontrado"),
        @ApiResponse(responseCode = "404", description = "Agente no encontrado")
    })
	AgentDTO getAgentById(@Parameter(description = "UUID del agente") @PathVariable UUID agentId) {return null;}
    
	ResponseEntity<Void> assignGroup(@ParameterObject UUID agentId, @ParameterObject UUID groupId) {return null;}
	ResponseEntity<Void> removeGroup(@ParameterObject UUID agentId, @ParameterObject UUID groupId) {return null;}
	ResponseEntity<Void> deleteAgent(@ParameterObject UUID agentId) {return null;}
	AgentDTO enableAgent(@ParameterObject UUID agentId) {return null;}
	AgentDTO disableAgent(@ParameterObject UUID agentId) {return null;}
}
