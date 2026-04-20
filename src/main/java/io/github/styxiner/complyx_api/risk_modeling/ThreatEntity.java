package io.github.styxiner.complyx_api.risk_modeling;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThreatEntity {
	private String name;
	private String description;
	private String category;
	private BigDecimal severityScore;
}
