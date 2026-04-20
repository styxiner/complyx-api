package io.github.styxiner.complyx_api.agents;

public interface AgentMapper {
	AgentDTO toDTO(AgentEntity agent);
	AgentEntity toEntity(AgentRegisterDTO agentRegisterDTO);
	
}
