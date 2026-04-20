package io.github.styxiner.complyx_api.policies;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PolicyRepository {
	Optional<PolicyEntity> findByName(String name);
	List<PolicyEntity> findBySeverity(Severity severity);
	List<PolicyEntity> findByAgentsId(UUID agentId);
	List<PolicyEntity> findByGroupsId(UUID groupId);
}
