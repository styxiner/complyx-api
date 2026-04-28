package io.github.styxiner.complyx_api.agents;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//Repositorio para grupos de agentes
public interface AgentGroupRepository extends JpaRepository<AgentGroupEntity, UUID>,JpaSpecificationExecutor<AgentGroupEntity>  {
	Optional<AgentGroupEntity> findByName(String name);
	boolean existsByName(String name);
}
