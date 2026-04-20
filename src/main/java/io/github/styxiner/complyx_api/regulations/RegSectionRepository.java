package io.github.styxiner.complyx_api.regulations;

import java.util.List;
import java.util.UUID;

public interface RegSectionRepository {
	List<RegulationSectionEntity> findByRegulationId(UUID regulationSectionId);
	List<RegulationSectionEntity> findByTitleContaining(String title);
}
