package io.github.styxiner.complyx_api.risk_modeling;

import java.math.BigDecimal;
import java.util.UUID;

public class RiskCreateDTO {
	private UUID threatId;
	private UUID agentId;
	private BigDecimal impact;
	public UUID getThreatId() {
		return threatId;
	}
	public UUID getAgentId() {
		return agentId;
	}
	public BigDecimal getImpact() {
		return impact;
	}
	public RiskCreateDTO(UUID threatId, UUID agentId, BigDecimal impact, BigDecimal probability) {
		super();
		this.threatId = threatId;
		this.agentId = agentId;
		this.impact = impact;
		this.probability = probability;
	}
	public BigDecimal getProbability() {
		return probability;
	}
	private BigDecimal probability;
}
