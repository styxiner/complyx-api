package io.github.styxiner.complyx_api.risk_modeling;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

public class RiskSpecifications {
	Specification<RiskEntity> byAgentId(UUID agentId) {return null;}
	Specification<RiskEntity> byStatus(RiskStatus riskStatus) {return null;}
	Specification<RiskEntity> byRiskLevel(RiskLevel riskLevel) {return null;}
	Specification<RiskEntity> byThreatId(UUID threatId) {return null;}
	Specification<RiskEntity> build(RiskFilter riskFilter) {return null;}
}
