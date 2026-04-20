package io.github.styxiner.complyx_api.agents;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

public class AgentSpecifications {
	Specification<AgentEntity> hasIp(String ip) {return null;}
	Specification<AgentEntity> hasHostname(String hostname) {return null;}
	Specification<AgentEntity> hasOsName(String nombre) {return null;}
	Specification<AgentEntity> isEnabled(boolean enabled) {return null;}
	Specification<AgentEntity> inGroup(UUID uuid) {return null;}
	Specification<AgentEntity> build(AgentFilter agentFilter) {return null;}
}
