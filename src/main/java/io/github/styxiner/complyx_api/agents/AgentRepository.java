package io.github.styxiner.complyx_api.agents;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AgentRepository extends JpaRepository<AgentEntity, UUID>, JpaSpecificationExecutor<AgentEntity> {

	Optional<AgentEntity> findByIp(String ip);

	boolean existsByIp(String ip);
}