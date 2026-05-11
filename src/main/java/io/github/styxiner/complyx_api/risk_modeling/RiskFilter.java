package io.github.styxiner.complyx_api.risk_modeling;

import java.util.UUID;

public class RiskFilter {
	private UUID agentId;
	private RiskStatus status;
	private RiskLevel riskLevel;
	private UUID threatId;
	public UUID getAgentId() {
		return agentId;
	}
	public RiskStatus getStatus() {
		return status;
	}
	public RiskLevel getRiskLevel() {
		return riskLevel;
	}
	public UUID getThreatId() {
		return threatId;
	}
	
	
}
