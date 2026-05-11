package io.github.styxiner.complyx_api.risk_modeling;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/threats")
@Tag(name = "Threats", description = "Gestión del catálogo de amenazas")
public class ThreatController {

    private final ThreatService threatService;

    public ThreatController(ThreatService threatService) {
        this.threatService = threatService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las amenazas con paginación")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Página de amenazas obtenida con éxito")
    })
    @Parameters({
        @Parameter(name = "page", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
        @Parameter(name = "size", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "20")),
        @Parameter(name = "sort", in = ParameterIn.QUERY, schema = @Schema(type = "string", example = "name,asc"))
    })
    public ResponseEntity<Page<ThreatDTO>> getThreats(
            @Parameter(hidden = true) @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(threatService.findAll(pageable));
    }

    @GetMapping("/{threatId}")
    @Operation(summary = "Obtener una amenaza por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Amenaza encontrada"),
        @ApiResponse(responseCode = "404", description = "Amenaza no encontrada")
    })
    public ResponseEntity<ThreatDTO> getThreatById(@PathVariable UUID threatId) {
        return ResponseEntity.ok(threatService.findById(threatId));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva amenaza")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Amenaza creada con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Ya existe una amenaza con ese nombre")
    })
    public ResponseEntity<ThreatDTO> createThreat(@Valid @RequestBody ThreatCreateDTO dto) {
        ThreatDTO created = threatService.create(dto);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(created.getId())
                        .toUri()
        ).body(created);
    }

    @PatchMapping("/{threatId}")
    @Operation(summary = "Actualizar parcialmente una amenaza")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Amenaza actualizada"),
        @ApiResponse(responseCode = "404", description = "Amenaza no encontrada")
    })
    public ResponseEntity<ThreatDTO> updateThreat(
            @PathVariable UUID threatId,
            @Valid @RequestBody ThreatUpdateDTO dto) {
        return ResponseEntity.ok(threatService.update(threatId, dto));
    }

    @DeleteMapping("/{threatId}")
    @Operation(summary = "Eliminar una amenaza")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Amenaza eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Amenaza no encontrada"),
        @ApiResponse(responseCode = "409", description = "Existen riesgos asociados a esta amenaza")
    })
    public ResponseEntity<Void> deleteThreat(@PathVariable UUID threatId) {
        threatService.delete(threatId);
        return ResponseEntity.noContent().build();
    }
}