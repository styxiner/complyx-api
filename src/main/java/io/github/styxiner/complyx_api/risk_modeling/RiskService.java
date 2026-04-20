package io.github.styxiner.complyx_api.risk_modeling;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class RiskService {
	Page<RiskDTO> findAll(RiskFilter riskFilter, Pageable pageable) {return null;}
	RiskDetailDTO findById(UUID riskId) {return null;}
	RiskDTO create(RiskCreateDTO riskCreateDto) {return null;}
	RiskDTO update(RiskUpdateDTO riskUpdateDto) {return null;}
	RiskDTO close(UUID riskId) {return null;}
	RiskDTO accept(UUID riskId) {return null;}
	void linkPolicy(UUID riskId, UUID policyId) {}
	void unLinkPolicy(UUID riskId, UUID policyId) {}
	RiskLevel computeRiskLevel(BigDecimal impact, BigDecimal probability) {return null;}	
}
