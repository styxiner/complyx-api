package io.github.styxiner.complyx_api.agents;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class AgentService {
	Page<AgentDTO> findAll(AgentFilter agentFilter, Pageable pageable) {return null;}
	AgentDTO findById(UUID agentId) {return null;}
	AgentDTO register(AgentRegisterDTO agentRegisterDTO) {return null;}
	void assignGroup(UUID agentId, UUID groupId) {}
	void removeGroup(UUID agentId, UUID groupId) {}
	void delete(UUID agentId) {}
}
