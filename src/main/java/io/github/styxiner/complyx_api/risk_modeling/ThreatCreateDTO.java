package io.github.styxiner.complyx_api.risk_modeling;

import java.math.BigDecimal;

public class ThreatCreateDTO {
	private String name;
	private String description;
	private String category;
	private BigDecimal severityScore;
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public String getCategory() {
		return category;
	}
	public BigDecimal getSeverityScore() {
		return severityScore;
	}
	public ThreatCreateDTO(String name, String description, String category, BigDecimal severityScore) {
		super();
		this.name = name;
		this.description = description;
		this.category = category;
		this.severityScore = severityScore;
	}
	
	
}
