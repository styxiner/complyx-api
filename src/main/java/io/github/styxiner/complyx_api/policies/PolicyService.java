package io.github.styxiner.complyx_api.policies;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PolicyService {
    PolicyDetailDTO createPolicy(PolicyCreateDTO dto);
    PolicyDetailDTO getPolicyById(UUID id);
    PolicyDetailDTO updatePolicy(UUID id, PolicyUpdateDTO dto);
    List<PolicySummaryDTO> getPoliciesByAgent(UUID agentId);
    Page<PolicySummaryDTO> getAllPolicies(PolicyFilter filter, Pageable pageable);    void deletePolicy(UUID id);
    void assignToAgent(UUID policyId, UUID agentId);
    void unAssignToAgent(UUID policyId, UUID agentId);
    void assignToGroup(UUID policyId, UUID groupId);
    void unAssignToGroup(UUID policyId, UUID groupId);
}