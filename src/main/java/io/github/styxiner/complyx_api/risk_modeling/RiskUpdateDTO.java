package io.github.styxiner.complyx_api.risk_modeling;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RiskUpdateDTO {
	private BigDecimal impact;
	private BigDecimal probability;
	private LocalDateTime reviewDate;
	public BigDecimal getImpact() {
		return impact;
	}
	public BigDecimal getProbability() {
		return probability;
	}
	public LocalDateTime getReviewDate() {
		return reviewDate;
	}
	public RiskUpdateDTO(BigDecimal impact, BigDecimal probability, LocalDateTime reviewDate) {
		super();
		this.impact = impact;
		this.probability = probability;
		this.reviewDate = reviewDate;
	}
}
