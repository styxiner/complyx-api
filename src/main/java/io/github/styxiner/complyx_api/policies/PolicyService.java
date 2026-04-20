package io.github.styxiner.complyx_api.policies;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PolicyService {
	Page<PolicySummaryDTO> findAll(PolicyFilter filter, Pageable pageable) {return null;}
	PolicyDetailDTO findById(UUID policyId) {return null;}
	PolicyDetailDTO create(PolicyCreateDTO policyCreateDto)  {return null;}
	PolicyDetailDTO update(UUID policyId, PolicyUpdateDTO policyUpdateDto) {return null;}
	void delete(UUID policyId) {}
	void assignToAgent(UUID policyId, UUID agentId) {}
	void unAssignToAgent(UUID policyId, UUID agentId) {}
	void assignToGroup(UUID policyId, UUID groupId) {}
	void unAssignToGroup(UUID policyId, UUID groupId) {}
	List<PolicySummaryDTO> getPoliciesByAgent(UUID agentId) {return null;}
}
