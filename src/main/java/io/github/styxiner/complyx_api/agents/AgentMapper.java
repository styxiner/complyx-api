package io.github.styxiner.complyx_api.agents;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AgentMapper {

	// Convierte la entidad a DTO y reduce los grupos a sus nombres.
	@Mapping(target = "groups", expression = "java(mapGroups(agent.getGroups()))")
	AgentDTO toDTO(AgentEntity agent);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "groups", ignore = true)
	@Mapping(target = "policies", ignore = true)
	@Mapping(target = "installDate", ignore = true)
	@Mapping(target = "latestConnection", ignore = true)
	@Mapping(target = "enabled", ignore = true)
	AgentEntity toEntity(AgentRegisterDTO agentRegisterDTO);

	default List<String> mapGroups(Set<AgentGroupEntity> groups) {
		List<String> groupNames = new ArrayList<>();

		if (groups == null) {
			return groupNames;
		}

		for (AgentGroupEntity group : groups) {
			groupNames.add(group.getName());
		}

		return groupNames;
	}
}