package io.github.styxiner.complyx_api.regulations;

import java.util.List;
import java.util.Optional;

public interface RegulationRepository {
	Optional<RegulationEntity> findByName(String name);
	List<RegulationEntity> findByNameContaining(String name);
}
