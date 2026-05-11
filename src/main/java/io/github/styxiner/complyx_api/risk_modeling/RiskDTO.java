package io.github.styxiner.complyx_api.risk_modeling;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class RiskDTO {
	private UUID id;
	private String threatName;
	private String agentHostname;
	private BigDecimal impact;
	private RiskLevel riskLevel;
	private RiskStatus status;
	private List<String> groups;
	
	public RiskDTO(UUID id, String threatName, String agentHostname, BigDecimal impact, RiskLevel riskLevel,
			RiskStatus status, List<String> groups) {
		super();
		this.id = id;
		this.threatName = threatName;
		this.agentHostname = agentHostname;
		this.impact = impact;
		this.riskLevel = riskLevel;
		this.status = status;
		this.groups = groups;
	}
	public UUID getId() {
		return id;
	}
	public String getThreatName() {
		return threatName;
	}
	public String getAgentHostname() {
		return agentHostname;
	}
	public BigDecimal getImpact() {
		return impact;
	}
	public RiskLevel getRiskLevel() {
		return riskLevel;
	}
	public RiskStatus getStatus() {
		return status;
	}
}
