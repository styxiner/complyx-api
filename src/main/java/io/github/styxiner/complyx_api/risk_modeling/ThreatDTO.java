package io.github.styxiner.complyx_api.risk_modeling;

import java.math.BigDecimal;
import java.util.UUID;

public class ThreatDTO {
	private UUID id;
	private String name;
	private String category;
	private BigDecimal severityScore;
}
