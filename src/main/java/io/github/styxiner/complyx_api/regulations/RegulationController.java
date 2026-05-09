package io.github.styxiner.complyx_api.regulations;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/regulations")
public class RegulationController {

    private final RegulationService regulationService;

    public RegulationController(RegulationService regulationService) {
        this.regulationService = regulationService;
    }

    @GetMapping
    @Parameters({
        @Parameter(name = "page", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
        @Parameter(name = "size", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "20")),
        @Parameter(name = "sort", in = ParameterIn.QUERY, schema = @Schema(type = "string", example = "name,asc"))
    })
    public Page<RegulationSummaryDTO> getRegulations(
            @ParameterObject RegulationFilter filter,
            @Parameter(hidden = true) Pageable pageable) {
        return regulationService.findAll(filter, pageable);
    }

    @GetMapping("/{regulationId}")
    public RegulationDetailDTO getRegulationById(@PathVariable UUID regulationId) {
        return regulationService.findById(regulationId);
    }

    @PostMapping
    public ResponseEntity<RegulationDetailDTO> createRegulation(@RequestBody RegulationCreateDTO dto) {
        return ResponseEntity.ok(regulationService.create(dto));
    }

    @PutMapping("/{regulationId}")
    public RegulationDetailDTO updateRegulation(
            @PathVariable UUID regulationId,
            @RequestBody RegulationUpdateDTO dto) {
        return regulationService.update(regulationId, dto);
    }

    @DeleteMapping("/{regulationId}")
    public ResponseEntity<Void> deleteRegulation(@PathVariable UUID regulationId) {
        regulationService.delete(regulationId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{regulationId}/pdf")
    public ResponseEntity<Void> uploadPdf(
            @PathVariable UUID regulationId,
            MultipartFile pdf) {
        regulationService.storePdf(regulationId, pdf);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{regulationId}/sections")
    public RegulationDetailDTO addSection(
            @PathVariable UUID regulationId,
            @RequestBody RegSectionCreateDTO dto) {
        return regulationService.addSection(regulationId, dto);
    }
}