package io.github.styxiner.complyx_api.agents;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/*
 * Interfaz que convierte objetos entre capas (AgentGroupEntity->AgentGroupDTO, AgentGroupCreateDTO->AgentGroupEntity)
 */
@Mapper(componentModel = "spring") // para que Spring pueda inyectarlo y MapStruct genere un @component
public interface AgentGroupMapper {

	// Convierte la Entidad (DB) al DTO de salida para el cliente
	@Mapping(target = "agentCount", expression = "java(mapAgents(group.getAgents()))")
	AgentGroupDTO toDTO(AgentGroupEntity group);

	// Convierte DTO de creación -> Entity
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "agents", ignore = true)
	AgentGroupEntity toEntity(AgentGroupCreateDTO dto);

	// Método auxiliar para mapear: Set<AgentEntity> -> List<String>

	default List<String> mapAgents(Set<AgentEntity> agents) {
		if (agents == null)
			return null;

		return agents.stream().map(AgentEntity::getHostname).collect(Collectors.toList());
	}
}