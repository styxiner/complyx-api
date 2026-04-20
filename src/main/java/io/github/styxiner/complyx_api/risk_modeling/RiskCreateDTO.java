package io.github.styxiner.complyx_api.risk_modeling;

import java.math.BigDecimal;
import java.util.UUID;

public class RiskCreateDTO {
	private UUID threatId;
	private UUID agentId;
	private BigDecimal impact;
	private BigDecimal probability;
}
