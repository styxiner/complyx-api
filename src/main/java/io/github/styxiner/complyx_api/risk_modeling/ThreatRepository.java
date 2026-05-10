package io.github.styxiner.complyx_api.risk_modeling;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ThreatRepository extends JpaRepository<ThreatEntity, UUID> {

    Optional<ThreatEntity> findByName(String name);

    List<ThreatEntity> findByCategory(String category);

    List<ThreatEntity> findBySeverityScoreGreaterThanEqual(BigDecimal score);

    boolean existsByName(String name);
}