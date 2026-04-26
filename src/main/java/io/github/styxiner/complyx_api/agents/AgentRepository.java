package io.github.styxiner.complyx_api.agents;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/*
 * Repositorio para la entidad AgentEntity.Spring Data JPA genera automáticamente la implementación.
 */
public interface AgentRepository extends
JpaRepository<AgentEntity, UUID>,               // CRUD
JpaSpecificationExecutor<AgentEntity> {			//soporte para filtros dinamicos

	Optional<AgentEntity> findByIp(String ip);

	boolean existsByIp(String ip);
}
