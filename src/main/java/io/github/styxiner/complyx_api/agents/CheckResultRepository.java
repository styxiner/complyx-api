package io.github.styxiner.complyx_api.agents;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CheckResultRepository extends JpaRepository<CheckResultEntity, UUID> {

    /**
     * Último resultado de cada check de una política para un agente dado.
     * DISTINCT ON (PostgreSQL) garantiza una fila por check_id, la más reciente.
     */
    @Query(value = """
        SELECT DISTINCT ON (cr.check_id) cr.*
        FROM   check_results cr
        JOIN   policy_checks   pc ON pc.id = cr.check_id
        JOIN   policy_elements pe ON pe.id = pc.policy_element_id
        WHERE  cr.agent_id  = :agentId
          AND  pe.policy_id = :policyId
        ORDER  BY cr.check_id, cr.executed_at DESC
        """, nativeQuery = true)
    List<CheckResultEntity> findLatestByAgentAndPolicy(
            @Param("agentId")  UUID agentId,
            @Param("policyId") UUID policyId);
}