package io.github.styxiner.complyx_api.risk_modeling;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/risks")
@RequiredArgsConstructor
public class RiskController {

    private final RiskService riskService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public Page<RiskDTO> getRisks(
            RiskFilter filter,
            @PageableDefault(size = 20, sort = "createdDate") Pageable pageable) {
        return riskService.findAll(filter, pageable);
    }

    @GetMapping("/{riskId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public RiskDetailDTO getRiskById(@PathVariable UUID riskId) {
        return riskService.findById(riskId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RiskDTO> createRisk(@Valid @RequestBody RiskCreateDTO dto) {
        RiskDTO created = riskService.create(dto);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PatchMapping("/{riskId}")
    @PreAuthorize("hasRole('ADMIN')")
    public RiskDTO updateRisk(
            @PathVariable UUID riskId,
            @Valid @RequestBody RiskUpdateDTO dto) {
        return riskService.update(riskId, dto);
    }

    // ---------------------------------------------------------------------------
    // Transiciones de estado
    // ---------------------------------------------------------------------------

    @PostMapping("/{riskId}/close")
    @PreAuthorize("hasRole('ADMIN')")
    public RiskDTO closeRisk(@PathVariable UUID riskId) {
        return riskService.close(riskId);
    }

    @PostMapping("/{riskId}/accept")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public RiskDTO acceptRisk(@PathVariable UUID riskId) {
        return riskService.accept(riskId);
    }

    @PostMapping("/{riskId}/transfer")
    @PreAuthorize("hasRole('ADMIN')")
    public RiskDTO transferRisk(@PathVariable UUID riskId) {
        return riskService.transfer(riskId);
    }

    @PostMapping("/{riskId}/monitor")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public RiskDTO monitorRisk(@PathVariable UUID riskId) {
        return riskService.setMonitoring(riskId);
    }

    // ---------------------------------------------------------------------------
    // Políticas mitigadoras
    // ---------------------------------------------------------------------------

    @PutMapping("/{riskId}/policies/{policyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> linkPolicy(
            @PathVariable UUID riskId,
            @PathVariable UUID policyId) {
        riskService.linkPolicy(riskId, policyId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{riskId}/policies/{policyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> unlinkPolicy(
            @PathVariable UUID riskId,
            @PathVariable UUID policyId) {
        riskService.unlinkPolicy(riskId, policyId);
        return ResponseEntity.noContent().build();
    }
}