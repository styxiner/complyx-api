package io.github.styxiner.complyx_api.agents;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplianceScoreRepository
        extends JpaRepository<ComplianceScoreEntity, ComplianceScoreId> {

    /** Scores de un agente para todos los elementos de una política. */
    List<ComplianceScoreEntity> findByAgentIdAndPolicy_Id(UUID agentId, UUID policyId);
}