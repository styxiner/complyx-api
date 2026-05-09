package io.github.styxiner.complyx_api.regulations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RegulationRepository extends JpaRepository<RegulationEntity, UUID>,
        JpaSpecificationExecutor<RegulationEntity> {
    Optional<RegulationEntity> findByName(String name);
    List<RegulationEntity> findByNameContaining(String name);
}