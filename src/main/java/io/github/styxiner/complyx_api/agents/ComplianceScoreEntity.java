package io.github.styxiner.complyx_api.agents;

import java.time.LocalDateTime;
import java.util.UUID;

import io.github.styxiner.complyx_api.policies.PolicyElementEntity;
import io.github.styxiner.complyx_api.policies.PolicyEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "compliance_scores")
@IdClass(ComplianceScoreId.class)
public class ComplianceScoreEntity {

	@Id
	@Column(name = "agent_id")
	private UUID agentId;

	@Id
	@Column(name = "policy_element_id")
	private UUID policyElementId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "agent_id", insertable = false, updatable = false)
	private AgentEntity agent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "policy_element_id", insertable = false, updatable = false)
	private PolicyElementEntity policyElement;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "policy_id")
	private PolicyEntity policy;

	@Column(name = "total_checks", nullable = false)
	private int totalChecks;

	@Column(name = "passed_checks", nullable = false)
	private int passedChecks;

	@Column(nullable = false)
	private double score;

	@Column(name = "last_updated", nullable = false)
	private LocalDateTime lastUpdated;

	public UUID getAgentId() {
		return agentId;
	}

	public UUID getPolicyElementId() {
		return policyElementId;
	}

	public AgentEntity getAgent() {
		return agent;
	}

	public PolicyElementEntity getPolicyElement() {
		return policyElement;
	}

	public PolicyEntity getPolicy() {
		return policy;
	}

	public int getTotalChecks() {
		return totalChecks;
	}

	public int getPassedChecks() {
		return passedChecks;
	}

	public double getScore() {
		return score;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}
}