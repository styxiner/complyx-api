package io.github.styxiner.complyx_api.risk_modeling;

import java.util.List;
import java.util.UUID;

public interface RiskRepository {
	List<RiskEntity> findByAgentId(UUID agentId);
	List<RiskEntity> findByStatus(RiskStatus status);
	List<RiskEntity> findByRiskLevel(RiskLevel level);
	List<RiskEntity> findByThreatId(UUID threatId);
}
