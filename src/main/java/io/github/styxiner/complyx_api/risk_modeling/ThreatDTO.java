package io.github.styxiner.complyx_api.risk_modeling;

import java.math.BigDecimal;
import java.util.UUID;

public class ThreatDTO {
	private UUID id;
	private String name;
	private String category;
	private BigDecimal severityScore;
	public UUID getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getCategory() {
		return category;
	}
	public BigDecimal getSeverityScore() {
		return severityScore;
	}
	public ThreatDTO(UUID id, String name, String category, BigDecimal severityScore) {
		super();
		this.id = id;
		this.name = name;
		this.category = category;
		this.severityScore = severityScore;
	}
	
	
}
