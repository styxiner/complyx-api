package io.github.styxiner.complyx_api.agents;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AgentGroupRepository {
	List<AgentGroupEntity> findByAgentIds(UUID uuid);
	Optional<AgentGroupEntity> findByName(String name);
}
