package io.github.styxiner.complyx_api.risk_modeling;

import java.net.URI;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/risks")
@Tag(name = "Risks", description = "Gestión del modelado de riesgos")
public class RiskController {

    private final RiskService riskService;

    public RiskController(RiskService riskService) {
        this.riskService = riskService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO', 'AUDITOR')")
    @GetMapping
    @Operation(summary = "Obtener todos los riesgos con filtros y paginación")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Página de riesgos obtenida con éxito")
    })
    @Parameters({
        @Parameter(name = "page", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
        @Parameter(name = "size", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "20")),
        @Parameter(name = "sort", in = ParameterIn.QUERY, schema = @Schema(type = "string", example = "createdDate,desc"))
    })
    public ResponseEntity<Page<RiskDTO>> getRisks(
            @ParameterObject RiskFilter filter,
            @Parameter(hidden = true) @PageableDefault(size = 20, sort = "createdDate") Pageable pageable) {
        return ResponseEntity.ok(riskService.findAll(filter, pageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO', 'AUDITOR')")
    @GetMapping("/{riskId}")
    @Operation(summary = "Obtener detalle de un riesgo por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Riesgo encontrado"),
        @ApiResponse(responseCode = "404", description = "Riesgo no encontrado")
    })
    public ResponseEntity<RiskDetailDTO> getRiskById(@PathVariable UUID riskId) {
        return ResponseEntity.ok(riskService.findById(riskId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    @PostMapping
    @Operation(summary = "Crear un nuevo riesgo")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Riesgo creado con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Amenaza o agente no encontrado")
    })
    public ResponseEntity<RiskDTO> createRisk(@Valid @RequestBody RiskCreateDTO dto) {
        RiskDTO created = riskService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    @PatchMapping("/{riskId}")
    @Operation(summary = "Actualizar parcialmente un riesgo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Riesgo actualizado"),
        @ApiResponse(responseCode = "404", description = "Riesgo no encontrado"),
        @ApiResponse(responseCode = "422", description = "El riesgo está cerrado y no puede modificarse")
    })
    public ResponseEntity<RiskDTO> updateRisk(
            @PathVariable UUID riskId,
            @Valid @RequestBody RiskUpdateDTO dto) {
        return ResponseEntity.ok(riskService.update(riskId, dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    @PostMapping("/{riskId}/close")
    @Operation(summary = "Cerrar un riesgo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Riesgo cerrado"),
        @ApiResponse(responseCode = "404", description = "Riesgo no encontrado"),
        @ApiResponse(responseCode = "422", description = "El riesgo ya está cerrado")
    })
    public ResponseEntity<RiskDTO> closeRisk(@PathVariable UUID riskId) {
        return ResponseEntity.ok(riskService.close(riskId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    @PostMapping("/{riskId}/accept")
    @Operation(summary = "Aceptar un riesgo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Riesgo aceptado"),
        @ApiResponse(responseCode = "404", description = "Riesgo no encontrado"),
        @ApiResponse(responseCode = "422", description = "El riesgo ya está cerrado")
    })
    public ResponseEntity<RiskDTO> acceptRisk(@PathVariable UUID riskId) {
        return ResponseEntity.ok(riskService.accept(riskId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    @PostMapping("/{riskId}/transfer")
    @Operation(summary = "Transferir un riesgo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Riesgo transferido"),
        @ApiResponse(responseCode = "404", description = "Riesgo no encontrado"),
        @ApiResponse(responseCode = "422", description = "El riesgo ya está cerrado")
    })
    public ResponseEntity<RiskDTO> transferRisk(@PathVariable UUID riskId) {
        return ResponseEntity.ok(riskService.transfer(riskId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    @PostMapping("/{riskId}/monitor")
    @Operation(summary = "Poner un riesgo en monitorización")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Riesgo en monitorización"),
        @ApiResponse(responseCode = "404", description = "Riesgo no encontrado"),
        @ApiResponse(responseCode = "422", description = "El riesgo ya está cerrado")
    })
    public ResponseEntity<RiskDTO> monitorRisk(@PathVariable UUID riskId) {
        return ResponseEntity.ok(riskService.setMonitoring(riskId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    @PutMapping("/{riskId}/policies/{policyId}")
    @Operation(summary = "Vincular una política mitigadora a un riesgo")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Política vinculada correctamente"),
        @ApiResponse(responseCode = "404", description = "Riesgo o política no encontrado")
    })
    public ResponseEntity<Void> linkPolicy(
            @PathVariable UUID riskId,
            @PathVariable UUID policyId) {
        riskService.linkPolicy(riskId, policyId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    @DeleteMapping("/{riskId}/policies/{policyId}")
    @Operation(summary = "Desvincular una política mitigadora de un riesgo")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Política desvinculada correctamente"),
        @ApiResponse(responseCode = "404", description = "Riesgo no encontrado")
    })
    public ResponseEntity<Void> unlinkPolicy(
            @PathVariable UUID riskId,
            @PathVariable UUID policyId) {
        riskService.unlinkPolicy(riskId, policyId);
        return ResponseEntity.noContent().build();
    }
}