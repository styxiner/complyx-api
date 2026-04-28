package io.github.styxiner.complyx_api.agents;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 * Servicio que contiene la lógica de negocio para los grupos de agentes (interactúa con entidades directamente)
 */
@Service
@Transactional(readOnly = true)
public class AgentGroupService {

	private final AgentGroupRepository groupRepository;
	private final AgentGroupMapper groupMapper;

	public AgentGroupService(AgentGroupRepository groupRepository, AgentGroupMapper groupMapper) {
		this.groupRepository = groupRepository;
		this.groupMapper = groupMapper;
	}

	// Obtiene todos los grupos paginados aplicando filtros dinamicos
	public Page<AgentGroupDTO> findAll(AgentGroupFilter filter,Pageable pageable) {
		// Construimos la especificacion a partir del filtro
        Specification<AgentGroupEntity> spec = AgentGroupSpecifications.build(filter);
        //Ejecutamos la query con paginación
        Page<AgentGroupEntity> page = groupRepository.findAll(spec, pageable);
        //Convertimos Entity -> DTO
        return page.map(groupMapper::toDTO);
	}

	// Obtiene un grupo por ID

	public AgentGroupDTO findById(UUID groupId) {
		AgentGroupEntity group = groupRepository.findById(groupId)
				.orElseThrow(() -> new RuntimeException("Group not found"));

		return groupMapper.toDTO(group);
	}

	// Crea un nuevo grupo
	@Transactional
	public AgentGroupDTO create(AgentGroupCreateDTO dto) {

		if (groupRepository.existsByName(dto.getName())) {
			throw new RuntimeException("Group with this name already exists");// Validación: nombre único
		}
		AgentGroupEntity group = groupMapper.toEntity(dto);// Convertimos DTO -> Entity
		AgentGroupEntity saved = groupRepository.save(group);// Guardamos en BD
		return groupMapper.toDTO(saved);// Devolvemos DTO
	}

	// Elimina un grupo
	@Transactional
	public void delete(UUID groupId) {
		if (!groupRepository.existsById(groupId)) {
			throw new RuntimeException("Group not found");
		}
		groupRepository.deleteById(groupId);
	}

	// Actualiza parcialmente un grupo
	@Transactional
	public AgentGroupDTO update(UUID groupId, AgentGroupUpdateDTO dto) {
		AgentGroupEntity group = groupRepository.findById(groupId)
				.orElseThrow(() -> new RuntimeException("Group not found"));
		// Actualizar nombre
		if (dto.getName() != null) { // si esque llega

			// Validar duplicado (solo si cambia)
			if (!dto.getName().equals(group.getName()) && groupRepository.existsByName(dto.getName())) {
				throw new RuntimeException("Group name already exists");
			}
			group.setName(dto.getName());
		}
		// Actualizar descripción
		if (dto.getDescription() != null) {
			group.setDescription(dto.getDescription());
		}
		// Guardar cambios
		AgentGroupEntity updated = groupRepository.save(group);
		return groupMapper.toDTO(updated);
	}
}