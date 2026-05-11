package io.github.styxiner.complyx_api.risk_modeling;

import java.math.BigDecimal;

public class ThreatUpdateDTO {
	private String description;
	private String category;
	private BigDecimal severityScore;
	public String getDescription() {
		return description;
	}
	public String getCategory() {
		return category;
	}
	public BigDecimal getSeverityScore() {
		return severityScore;
	}
	public ThreatUpdateDTO(String description, String category, BigDecimal severityScore) {
		super();
		this.description = description;
		this.category = category;
		this.severityScore = severityScore;
	}
	
	
}
