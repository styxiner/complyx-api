	package io.github.styxiner.complyx_api.agents;
	
	import java.util.List;
	import java.util.Optional;
	import java.util.UUID;
	
	public interface AgentRepository {
		Optional<AgentEntity> findByIp(String ip);
		List<AgentEntity> findByEnabled(boolean enabled);
		List<AgentEntity> findByGroupsId(UUID uuid);
	}
