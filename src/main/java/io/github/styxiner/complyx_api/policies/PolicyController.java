package io.github.styxiner.complyx_api.policies;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public class PolicyController {
	Page<PolicySummaryDTO> getPolicies(PolicyFilter filter, Pageable pageable) {return null;}
	PolicyDetailDTO getPolicyById(UUID policyId) {return null;}
	ResponseEntity<PolicyDetailDTO> createPolicy(PolicyCreateDTO policyCreateDTO) {return null;}
	PolicyDetailDTO updatePolicy(UUID policyId) {return null;}
	ResponseEntity<Void> deletePolicy(UUID policyId) {return null;}
	ResponseEntity<Void> assignToAgent(UUID policyId, UUID agentId) {return null;}
	ResponseEntity<Void> assignToGroup(UUID policyId, UUID groupId) {return null;}
	ResponseEntity<Void> unAssignToAgent(UUID policyId, UUID agentId) {return null;}
	ResponseEntity<Void> unAssignToGroup(UUID policyId, UUID groupId) {return null;}
	List<PolicySummaryDTO> getPoliciesByAgent(UUID agentId) {return null;}
}
