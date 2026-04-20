package io.github.styxiner.complyx_api.risk_modeling;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public class RiskController {
	Page<RiskDTO> getRisks(RiskFilter riskFilter, Pageable pageable) {return null;}
	ThreatDTO getThreatById(UUID threatId) {return null;}
	ResponseEntity<ThreatDTO> createThreat(ThreatCreateDTO threatCreateDto) {return null;}
	RiskDTO updateRisk(UUID riskId, RiskUpdateDTO riskUpdateDto) {return null;}
	RiskDTO closeRisk(UUID riskId) {return null;}
	RiskDTO acceptRisk(UUID riskId) {return null;}
	ResponseEntity<Void> linkPolicy(UUID riskId, UUID policyId) {return null;}
	ResponseEntity<Void> unlinkPolicy(UUID riskId, UUID policyId) {return null;}
}
