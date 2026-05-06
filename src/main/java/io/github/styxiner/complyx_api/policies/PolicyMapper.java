package io.github.styxiner.complyx_api.policies;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


/*
 * Clase "mapeadora" de politica usando Mapstruct para convertir Entity->DTO, se
 * usa en listados paginados
 */
@Mapper(componentModel = "spring") // para que Spring pueda inyectarlo y MapStruct genere un @component
public interface PolicyMapper {
	// Convierte PolicyEntity a DTO de resumen.
	@Mapping(target = "status", source = "status")
	@Mapping(target = "createdAt", source = "createdDate")
	PolicySummaryDTO toSummaryDTO(PolicyEntity entity);

// Convierte PolicyEntity a DTO de detalle completo.
	@Mapping(target = "status", source = "status")
	@Mapping(target = "createdAt", source = "createdDate")
	PolicyDetailDTO toDetailDTO(PolicyEntity entity);

	/**
	 * Convierte listas de entidades a lista de DTOs simples ydetalle .
	 */
	List<PolicyDetailDTO> toDetailDTOList(List<PolicyEntity> entities);
	List<PolicySummaryDTO> toSummaryList(List<PolicyEntity> entities);
}
