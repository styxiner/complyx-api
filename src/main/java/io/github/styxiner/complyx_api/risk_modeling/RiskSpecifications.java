package io.github.styxiner.complyx_api.risk_modeling;

import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class RiskSpecifications {

    private RiskSpecifications() {}

    public static Specification<RiskEntity> byAgentId(UUID agentId) {
        return (root, query, cb) ->
            agentId == null ? null : cb.equal(root.get("agent").get("id"), agentId);
    }

    /**
     * Compara status como String en minúsculas para coincidir con el constraint de BD
     * que almacena 'open', 'accepted', etc.
     */
    public static Specification<RiskEntity> byStatus(RiskStatus riskStatus) {
        return (root, query, cb) ->
            riskStatus == null ? null :
                cb.equal(root.get("status").as(String.class),
                         riskStatus.name().toLowerCase());
    }

    /**
     * Compara riskLevel como String en minúsculas para coincidir con el constraint de BD
     * que almacena 'low', 'medium', 'high', 'critical'.
     */
    public static Specification<RiskEntity> byRiskLevel(RiskLevel riskLevel) {
        return (root, query, cb) ->
            riskLevel == null ? null :
                cb.equal(root.get("riskLevel").as(String.class),
                         riskLevel.name().toLowerCase());
    }

    public static Specification<RiskEntity> byThreatId(UUID threatId) {
        return (root, query, cb) ->
            threatId == null ? null : cb.equal(root.get("threat").get("id"), threatId);
    }

    public static Specification<RiskEntity> build(RiskFilter riskFilter) {
        if (riskFilter == null) {
            return Specification.allOf();
        }
        return Specification.allOf(
                byAgentId(riskFilter.getAgentId()),
                byStatus(riskFilter.getStatus()),
                byRiskLevel(riskFilter.getRiskLevel()),
                byThreatId(riskFilter.getThreatId())
        );
    }
}