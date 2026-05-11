package io.github.styxiner.complyx_api.regulations;

import java.net.URI;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/api/regulations")
@Tag(name = "Regulations", description = "Gestión de normativas y sus secciones")
public class RegulationController {

    private final RegulationService regulationService;

    public RegulationController(RegulationService regulationService) {
        this.regulationService = regulationService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las normativas con filtros y paginación")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Página de normativas obtenida con éxito")
    })
    @Parameters({
        @Parameter(name = "page", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
        @Parameter(name = "size", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "20")),
        @Parameter(name = "sort", in = ParameterIn.QUERY, schema = @Schema(type = "string", example = "name,asc"))
    })
    public ResponseEntity<Page<RegulationSummaryDTO>> getRegulations(
            @ParameterObject RegulationFilter filter,
            @Parameter(hidden = true) Pageable pageable) {
        return ResponseEntity.ok(regulationService.findAll(filter, pageable));
    }

    @GetMapping("/{regulationId}")
    @Operation(summary = "Obtener detalle de una normativa por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Normativa encontrada"),
        @ApiResponse(responseCode = "404", description = "Normativa no encontrada")
    })
    public ResponseEntity<RegulationDetailDTO> getRegulationById(@PathVariable UUID regulationId) {
        return ResponseEntity.ok(regulationService.findById(regulationId));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva normativa")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Normativa creada con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Ya existe una normativa con ese nombre")
    })
    public ResponseEntity<RegulationDetailDTO> createRegulation(@Valid @RequestBody RegulationCreateDTO dto) {
        RegulationDetailDTO created = regulationService.create(dto);
        URI location = URI.create("/api/regulations/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{regulationId}")
    @Operation(summary = "Actualizar una normativa existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Normativa actualizada"),
        @ApiResponse(responseCode = "404", description = "Normativa no encontrada")
    })
    public ResponseEntity<RegulationDetailDTO> updateRegulation(
            @PathVariable UUID regulationId,
            @Valid @RequestBody RegulationUpdateDTO dto) {
        return ResponseEntity.ok(regulationService.update(regulationId, dto));
    }

    @DeleteMapping("/{regulationId}")
    @Operation(summary = "Eliminar una normativa")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Normativa eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Normativa no encontrada")
    })
    public ResponseEntity<Void> deleteRegulation(@PathVariable UUID regulationId) {
        regulationService.delete(regulationId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{regulationId}/pdf")
    @Operation(summary = "Subir el PDF asociado a una normativa")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "PDF almacenado correctamente"),
        @ApiResponse(responseCode = "404", description = "Normativa no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error al almacenar el PDF")
    })
    public ResponseEntity<Void> uploadPdf(
            @PathVariable UUID regulationId,
            @RequestParam("pdf") MultipartFile pdf) {
        regulationService.storePdf(regulationId, pdf);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{regulationId}/sections")
    @Operation(summary = "Añadir una sección a una normativa")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sección añadida correctamente"),
        @ApiResponse(responseCode = "404", description = "Normativa no encontrada")
    })
    public ResponseEntity<RegulationDetailDTO> addSection(
            @PathVariable UUID regulationId,
            @Valid @RequestBody RegSectionCreateDTO dto) {
        return ResponseEntity.ok(regulationService.addSection(regulationId, dto));
    }
}