package io.github.styxiner.complyx_api.risk_modeling;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import io.github.styxiner.complyx_api.policies.PolicyEntity;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiskEntity {
	private BigDecimal impact;
	private BigDecimal probability;
	private RiskLevel riskLevel;
	private RiskStatus riskStatus;
	private LocalDateTime reviewDate;
	private LocalDateTime acceptanceDate;
	private Set<PolicyEntity> policies;
}
