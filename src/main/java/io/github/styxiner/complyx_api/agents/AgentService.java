package io.github.styxiner.complyx_api.agents;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AgentService {
	private final AgentRepository agentRepository;
	private final AgentGroupRepository groupRepository;
	private final AgentMapper agentMapper;

	public AgentService(AgentRepository agentRepository, AgentGroupRepository groupRepository,
			AgentMapper agentMapper) {
		super();
		this.agentRepository = agentRepository;
		this.groupRepository = groupRepository;
		this.agentMapper = agentMapper;
	}

// Obtiene una página de agentes filtrados y los convierte a DTO.
	public Page<AgentDTO> findAll(AgentFilter filter, Pageable pageable) {
		Specification<AgentEntity> spec = AgentSpecifications.build(filter);
		return agentRepository.findAll(spec, pageable).map(agentMapper::toDTO);
	}

// Busca un agente por su UUID o lanza una excepción si no existe.
	AgentDTO findById(UUID agentId) {
		AgentEntity agent = agentRepository.findById(agentId)
				.orElseThrow(() -> new RuntimeException("Agent not found"));

		return agentMapper.toDTO(agent);// Devuelve el agente convertido a DTO
	}

// Registra un nuevo agente validando que la IP sea única y asignando valores iniciales.
	@Transactional
	public AgentDTO register(AgentRegisterDTO agentRegisterDTO) { // Convertimos el DTO de registro en una Entidad de
																	// base de datos dado que manipulamos la BD es
																	// necesario @Transaccional
		if (agentRepository.existsByIp(agentRegisterDTO.getIp())) {
			throw new RuntimeException("Agent with this IP already exists");
		}

		AgentEntity agent = agentMapper.toEntity(agentRegisterDTO);
		agent.setEnabled(true);
		agent.setInstallDate(LocalDateTime.now());
		agent.setLatestConnection(LocalDateTime.now());

		AgentEntity saved = agentRepository.save(agent);

		return agentMapper.toDTO(saved);
	}
// Asocia un agente a un grupo específico.
	@Transactional
	void assignGroup(UUID agentId, UUID groupId) {
		AgentEntity agent = agentRepository.findById(agentId)
				.orElseThrow(() -> new RuntimeException("Agent not found"));

		AgentGroupEntity group = groupRepository.findById(groupId)
				.orElseThrow(() -> new RuntimeException("Group not found"));
// Buscamos ambos objetos. Si alguno falla, se cancela la operación.
		agent.addGroup(group);
		agentRepository.save(agent);
//Añadimos el grupo y persistimos el cambio
	}
// Desvincula un agente de un grupo específico.
	void removeGroup(UUID agentId, UUID groupId) {
		AgentEntity agent = agentRepository.findById(agentId)
				.orElseThrow(() -> new RuntimeException("Agent not found"));
		AgentGroupEntity group = groupRepository.findById(groupId)
				.orElseThrow(() -> new RuntimeException("Group not found"));
		agent.removeGroup(group);
		agentRepository.save(agent);
	}
// Elimina un agente del sistema tras verificar su existencia.
	@Transactional
	public void delete(UUID agentId) {
		if (!agentRepository.existsById(agentId)) {
			throw new RuntimeException("Agent not found");
		}
		agentRepository.deleteById(agentId);
	}

	// Activa un agente permitiendo que opere en el sistema.
	@Transactional
	public AgentDTO enable(UUID agentId) {

		AgentEntity agent = agentRepository.findById(agentId)
				.orElseThrow(() -> new RuntimeException("Agent not found"));

		agent.setEnabled(true);

		return agentMapper.toDTO(agentRepository.save(agent));
	}
// Desactiva un agente restringiendo sus operaciones.
	@Transactional
	public AgentDTO disable(UUID agentId) {

		AgentEntity agent = agentRepository.findById(agentId)
				.orElseThrow(() -> new RuntimeException("Agent not found"));

		agent.setEnabled(false);

		return agentMapper.toDTO(agentRepository.save(agent));
	}

}
