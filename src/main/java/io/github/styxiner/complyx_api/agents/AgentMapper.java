package io.github.styxiner.complyx_api.agents;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/*
 * Interfaz que convierte objetos entre capas (AgentEntity<->AgentDTO, AgentRegisterDTO->AgentEntity)
 * MapStruct genera la implementacion automaticamente
 */
@Mapper(componentModel = "spring") // para que Spring pueda inyectarlo y MapStruct genere un @component
public interface AgentMapper {
	// Convierte AgentEntity->AgentDTO
	@Mapping(target = "groups", expression = "java(mapGroups(agent.getGroups()))")
	AgentDTO toDTO(AgentEntity agent);

	// Convierte DTO de registro -> Entity
	@Mapping(target = "id", ignore = true) // ignore=true puesto que el ID lo genera la BD
	@Mapping(target = "groups", ignore = true) // ignore=true pues se asignan en el service
	@Mapping(target = "installDate", ignore = true)
	@Mapping(target = "latestConnection", ignore = true)
	@Mapping(target = "enabled", ignore = true)
	AgentEntity toEntity(AgentRegisterDTO agentRegisterDTO);

	/*
	 * Definimos manualmente este metodo para mapear, como tiene implementacion
	 * usamos default para que la interfaz lo use. Este metodo convierte
	 * Set<AgentGroupEntity> (Entity) en un stream, transforma cada elemento y
	 * convierte el stream en una coleccion -> List<String> (DTO),
	 */

	default List<String> mapGroups(Set<AgentGroupEntity> groups) {
		if (groups == null)
			return null;
		return groups.stream().map(AgentGroupEntity::getName).collect(Collectors.toList());
	}
}