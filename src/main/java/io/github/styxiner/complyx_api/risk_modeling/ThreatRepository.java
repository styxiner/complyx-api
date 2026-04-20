package io.github.styxiner.complyx_api.risk_modeling;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ThreatRepository {
	Optional<ThreatEntity> findByName(String name);
	List<ThreatEntity> findByCategory(String category);
	List<ThreatEntity> findBySeverityScoreGreaterThanEqual(BigDecimal score);
}
