package io.github.styxiner.complyx_api.risk_modeling;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import io.github.styxiner.complyx_api.agents.AgentDTO;
import io.github.styxiner.complyx_api.policies.PolicySummaryDTO;

public class RiskDetailDTO {
	private UUID id;
	private ThreatDTO threat;
	private AgentDTO agent;
	private BigDecimal impact;
	private BigDecimal probability;
	private RiskLevel riskLevel;
	
	public RiskDetailDTO(UUID id, ThreatDTO threat, AgentDTO agent, BigDecimal impact, BigDecimal probability,
			RiskLevel riskLevel, RiskStatus riskStatus, List<PolicySummaryDTO> policySummaryDto,
			LocalDateTime createdDate) {
		super();
		this.id = id;
		this.threat = threat;
		this.agent = agent;
		this.impact = impact;
		this.probability = probability;
		this.riskLevel = riskLevel;
		this.riskStatus = riskStatus;
		this.policySummaryDto = policySummaryDto;
		this.createdDate = createdDate;
	}
	public UUID getId() {
		return id;
	}
	public ThreatDTO getThreat() {
		return threat;
	}
	public AgentDTO getAgent() {
		return agent;
	}
	public BigDecimal getImpact() {
		return impact;
	}
	public BigDecimal getProbability() {
		return probability;
	}
	public RiskLevel getRiskLevel() {
		return riskLevel;
	}
	public RiskStatus getRiskStatus() {
		return riskStatus;
	}
	public List<PolicySummaryDTO> getPolicySummaryDto() {
		return policySummaryDto;
	}
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	private RiskStatus riskStatus;
	private List<PolicySummaryDTO> policySummaryDto;
	private LocalDateTime createdDate;
}
