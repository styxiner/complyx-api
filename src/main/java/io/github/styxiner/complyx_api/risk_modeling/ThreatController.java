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
@RequestMapping("/api/threats")
public class ThreatController {

    private final ThreatService threatService;

    public ThreatController(ThreatService threatService) {
		this.threatService = threatService;
	}

	@GetMapping
    public Page<ThreatDTO> getThreats(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return threatService.findAll(pageable);
    }

    @GetMapping("/{threatId}")
    public ThreatDTO getThreatById(@PathVariable UUID threatId) {
        return threatService.findById(threatId);
    }

    @PostMapping
    public ResponseEntity<ThreatDTO> createThreat(@Valid @RequestBody ThreatCreateDTO dto) {
        ThreatDTO created = threatService.create(dto);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PatchMapping("/{threatId}")
    public ThreatDTO updateThreat(
            @PathVariable UUID threatId,
            @Valid @RequestBody ThreatUpdateDTO dto) {
        return threatService.update(threatId, dto);
    }

    @DeleteMapping("/{threatId}")
    public ResponseEntity<Void> deleteThreat(@PathVariable UUID threatId) {
        threatService.delete(threatId);
        return ResponseEntity.noContent().build();
    }
}