package io.github.styxiner.complyx_api.agents;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentGroupRepository extends JpaRepository<AgentGroupEntity, UUID> {
	Optional<AgentGroupEntity> findByName(String name);
	boolean existByName(String name);
}
