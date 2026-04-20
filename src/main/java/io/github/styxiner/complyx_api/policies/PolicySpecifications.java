package io.github.styxiner.complyx_api.policies;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

public class PolicySpecifications {
	Specification<PolicyEntity> hasName(String name) {return null;}
	Specification<PolicyEntity> hasSeverity(Severity severity) {return null;}
	Specification<PolicyEntity> assignedToAgent(UUID agentId) {return null;}
	Specification<PolicyEntity> assignedToGroup(UUID groupId) {return null;}
	Specification<PolicyEntity> build(PolicyFilter filter)	 {return null;}
}
