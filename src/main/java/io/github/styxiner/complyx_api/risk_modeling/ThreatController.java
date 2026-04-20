package io.github.styxiner.complyx_api.risk_modeling;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public class ThreatController {
	Page<ThreatDTO> getThreats(Pageable pageable){return null;}
	ThreatDTO getThreatById(UUID threatId) {return null;}
	ResponseEntity<ThreatDTO> createThreat(ThreatCreateDTO threatCreateDto) {return null;}
	ThreatDTO updateThreat(UUID threatId, ThreatUpdateDTO threatUpdateDto) {return null;}
	ResponseEntity<Void> deleteThreat(UUID threatId) {return null;}
}
