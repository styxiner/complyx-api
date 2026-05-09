package io.github.styxiner.complyx_api.regulations;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegSectionRepository extends JpaRepository<RegulationSectionEntity, UUID> {
    List<RegulationSectionEntity> findByRegulationId(UUID regulationId);
    List<RegulationSectionEntity> findByTitleContaining(String title);
}