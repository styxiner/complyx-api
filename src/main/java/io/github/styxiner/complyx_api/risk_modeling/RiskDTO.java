package io.github.styxiner.complyx_api.risk_modeling;

import java.math.BigDecimal;
import java.util.UUID;

public class RiskDTO {
	private UUID id;
	private String threatName;
	private String agentHostname;
	private BigDecimal impact;
	private RiskLevel riskLevel;
	private RiskStatus status;
}
