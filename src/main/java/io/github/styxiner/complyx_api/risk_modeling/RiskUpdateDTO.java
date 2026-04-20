package io.github.styxiner.complyx_api.risk_modeling;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RiskUpdateDTO {
	private BigDecimal impact;
	private BigDecimal probability;
	private LocalDateTime reviewDate;
}
