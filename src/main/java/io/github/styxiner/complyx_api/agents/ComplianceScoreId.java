package io.github.styxiner.complyx_api.agents;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class ComplianceScoreId implements Serializable {
	private UUID agentId;
	private UUID policyElementId;

	public ComplianceScoreId() {
	}

	public ComplianceScoreId(UUID agentId, UUID policyElementId) {
		this.agentId = agentId;
		this.policyElementId = policyElementId;
	}

	public UUID getAgentId() {
		return agentId;
	}

	public UUID getPolicyElementId() {
		return policyElementId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ComplianceScoreId))
			return false;
		ComplianceScoreId that = (ComplianceScoreId) o;
		return Objects.equals(agentId, that.agentId) && Objects.equals(policyElementId, that.policyElementId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(agentId, policyElementId);
	}
}