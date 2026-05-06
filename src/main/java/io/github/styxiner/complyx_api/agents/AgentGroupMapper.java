package io.github.styxiner.complyx_api.agents;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/*
 * Interfaz que convierte objetos entre capas (AgentGroupEntity->AgentGroupDTO, AgentGroupCreateDTO->AgentGroupEntity)
 */
@Mapper(componentModel = "spring") // para que Spring pueda inyectarlo y MapStruct genere un @component
public interface AgentGroupMapper {

	// Convierte la Entidad (DB) al DTO de salida para el cliente
	@Mapping(target = "agents", expression = "java(mapAgents(group.getAgents()))")
	AgentGroupDTO toDTO(AgentGroupEntity group);

	// Convierte DTO de creaci�n -> Entity
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "agents", ignore = true)
	@Mapping(target = "policies", ignore = true)
	AgentGroupEntity toEntity(AgentGroupCreateDTO dto);

	// M�todo auxiliar para mapear: Set<AgentEntity> -> List<String>

	default List<String> mapAgents(Set<AgentEntity> agents) {
		List<String> agentNames = new ArrayList<>();

		if (agents == null) {
			return agentNames;
		}

		for (AgentEntity agent : agents) {
			agentNames.add(agent.getHostname());
		}

		return agentNames;
	}

}