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
	private RiskStatus riskStatus;
	private List<PolicySummaryDTO> policySummaryDto;
	private LocalDateTime createdDate;
}
