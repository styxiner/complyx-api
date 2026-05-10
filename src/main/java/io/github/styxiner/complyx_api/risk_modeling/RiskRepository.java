package io.github.styxiner.complyx_api.risk_modeling;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RiskRepository
        extends JpaRepository<RiskEntity, UUID>,
                JpaSpecificationExecutor<RiskEntity> {

    List<RiskEntity> findByAgentId(UUID agentId);

    List<RiskEntity> findByStatus(RiskStatus status);

    List<RiskEntity> findByRiskLevel(RiskLevel level);

    List<RiskEntity> findByThreatId(UUID threatId);

    Page<RiskEntity> findByAgentId(UUID agentId, Pageable pageable);

    // Riesgos abiertos de un agente para una amenaza concreta
    // (usado por el risk_trigger del servidor Rust para evitar duplicados)
    @Query("""
        SELECT r FROM RiskEntity r
        WHERE r.agent.id = :agentId
          AND r.threat.id = :threatId
          AND r.status = 'OPEN'
        """)
    List<RiskEntity> findOpenByAgentIdAndThreatId(
            @Param("agentId") UUID agentId,
            @Param("threatId") UUID threatId);

    // Añadir política mitigadora
    @Modifying
    @Query(value = """
        INSERT INTO risk_policies (risk_id, policy_id)
        VALUES (:riskId, :policyId)
        ON CONFLICT DO NOTHING
        """, nativeQuery = true)
    void linkPolicy(@Param("riskId") UUID riskId, @Param("policyId") UUID policyId);

    // Eliminar política mitigadora
    @Modifying
    @Query(value = """
        DELETE FROM risk_policies
        WHERE risk_id = :riskId AND policy_id = :policyId
        """, nativeQuery = true)
    void unlinkPolicy(@Param("riskId") UUID riskId, @Param("policyId") UUID policyId);
}