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

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/risks")
public class RiskController {

    private final RiskService riskService;

    public RiskController(RiskService riskService) {
		this.riskService = riskService;
	}

	@GetMapping
    public Page<RiskDTO> getRisks(
            RiskFilter filter,
            @PageableDefault(size = 20, sort = "createdDate") Pageable pageable) {
        return riskService.findAll(filter, pageable);
    }

    @GetMapping("/{riskId}")
    public RiskDetailDTO getRiskById(@PathVariable UUID riskId) {
        return riskService.findById(riskId);
    }

    @PostMapping
    public ResponseEntity<RiskDTO> createRisk(@Valid @RequestBody RiskCreateDTO dto) {
        RiskDTO created = riskService.create(dto);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created((URI) location).body(created);
    }

    @PatchMapping("/{riskId}")
    public RiskDTO updateRisk(
            @PathVariable UUID riskId,
            @Valid @RequestBody RiskUpdateDTO dto) {
        return riskService.update(riskId, dto);
    }

    @PostMapping("/{riskId}/close")
    public RiskDTO closeRisk(@PathVariable UUID riskId) {
        return riskService.close(riskId);
    }

    @PostMapping("/{riskId}/accept")
    public RiskDTO acceptRisk(@PathVariable UUID riskId) {
        return riskService.accept(riskId);
    }

    @PostMapping("/{riskId}/transfer")
    public RiskDTO transferRisk(@PathVariable UUID riskId) {
        return riskService.transfer(riskId);
    }

    @PostMapping("/{riskId}/monitor")
    public RiskDTO monitorRisk(@PathVariable UUID riskId) {
        return riskService.setMonitoring(riskId);
    }

    @PutMapping("/{riskId}/policies/{policyId}")
    public ResponseEntity<Void> linkPolicy(
            @PathVariable UUID riskId,
            @PathVariable UUID policyId) {
        riskService.linkPolicy(riskId, policyId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{riskId}/policies/{policyId}")
    public ResponseEntity<Void> unlinkPolicy(
            @PathVariable UUID riskId,
            @PathVariable UUID policyId) {
        riskService.unlinkPolicy(riskId, policyId);
        return ResponseEntity.noContent().build();
    }
}