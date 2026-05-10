package io.github.styxiner.complyx_api.risk_modeling;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RiskSpecifications {

    private RiskSpecifications() {}

    public static Specification<RiskEntity> byAgentId(UUID agentId) {
        return (root, query, cb) ->
            agentId == null ? null : cb.equal(root.get("agent").get("id"), agentId);
    }

    public static Specification<RiskEntity> byStatus(RiskStatus riskStatus) {
        return (root, query, cb) ->
            riskStatus == null ? null : cb.equal(root.get("status"), riskStatus);
    }

    public static Specification<RiskEntity> byRiskLevel(RiskLevel riskLevel) {
        return (root, query, cb) ->
            riskLevel == null ? null : cb.equal(root.get("riskLevel"), riskLevel);
    }

    public static Specification<RiskEntity> byThreatId(UUID threatId) {
        return (root, query, cb) ->
            threatId == null ? null : cb.equal(root.get("threat").get("id"), threatId);
    }

    /**
     * Combina todos los filtros del RiskFilter aplicando solo los no nulos.
     * El resultado es un AND de todos los predicados activos.
     */
    public static Specification<RiskEntity> build(RiskFilter riskFilter) {
        if (riskFilter == null) return Specification.where(null);

        return Specification.where(byAgentId(riskFilter.getAgentId()))
                .and(byStatus(riskFilter.getStatus()))
                .and(byRiskLevel(riskFilter.getRiskLevel()))
                .and(byThreatId(riskFilter.getThreatId()));
    }
}