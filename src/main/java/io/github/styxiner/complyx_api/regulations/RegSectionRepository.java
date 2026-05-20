package io.github.styxiner.complyx_api.regulations;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RegSectionRepository extends JpaRepository<RegulationSectionEntity, UUID> {
    List<RegulationSectionEntity> findByRegulationId(UUID regulationId);
    List<RegulationSectionEntity> findByTitleContaining(String title);
    
    @Modifying
    @Query(value = """
        DELETE FROM check_regulation_sections
        WHERE regulation_section_id IN (
            SELECT id FROM regulation_sections WHERE regulation_id = :regulationId
        )
        """, nativeQuery = true)
    void deleteCheckLinksByRegulationId(@Param("regulationId") UUID regulationId);
}