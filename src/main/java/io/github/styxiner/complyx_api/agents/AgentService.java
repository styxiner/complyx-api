package io.github.styxiner.complyx_api.agents;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

@Service
@Transactional(readOnly = true)
public class AgentService {
	Page<AgentDTO> findAll(AgentFilter agentFilter, Pageable pageable) {return null;}
	AgentDTO findById(UUID agentId) {return null;}
	AgentDTO register(AgentRegisterDTO agentRegisterDTO) {return null;}
	void assignGroup(UUID agentId, UUID groupId) {}
	void removeGroup(UUID agentId, UUID groupId) {}
	void delete(UUID agentId) {}

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

	/*
	 * Usamos una clase an�nima para crear un objeto que implementa una interfaz en
	 * particular y poder usarlo libremente sin definir expl�citamente m�s clases y no usar lambdas
	 */
// Obtiene una p�gina de agentes filtrados y los convierte a DTO.
	public Page<AgentDTO> findAll(AgentFilter filter, Pageable pageable) {
		Specification<AgentEntity> spec = AgentSpecifications.build(filter);
		return agentRepository.findAll(spec, pageable).map(new Function<AgentEntity, AgentDTO>() {
			@Override
			public AgentDTO apply(AgentEntity agent) {
				return agentMapper.toDTO(agent);
			}
		});
	}

	/*
	 * En este caso se usa para implementar Supplier<T>, que es requerido por
	 * orElseThrow().
	 */
// Busca un agente por su UUID o lanza una excepci�n si no existe.
	AgentDTO findById(UUID agentId) {
		AgentEntity agent = agentRepository.findById(agentId).orElseThrow(new Supplier<RuntimeException>() {
			@Override
			public RuntimeException get() {
				return new RuntimeException("Agent not found");
			}
		});
		return agentMapper.toDTO(agent);// Devuelve el agente convertido a DTO
	}

// Registra un nuevo agente validando que la IP sea �nica y asignando valores iniciales.
	@Transactional
	public AgentDTO register(AgentRegisterDTO agentRegisterDTO) { // Convertimos el DTO de registro
																						// en una Entidad de
		// base de datos dado que manipulamos la BD es
		// necesario @Transaccional
		if (agentRepository.existsByIp(agentRegisterDTO.getIp())) {
			throw new RuntimeException("Agent with this IP already exists");
		}
		AgentEntity agent = agentMapper.toEntity(agentRegisterDTO);
		AgentEntity saved = agentRepository.save(agent);

		return agentMapper.toDTO(saved);
	}

// Asocia un agente a un grupo espec�fico.
	@Transactional
	void assignGroup(UUID agentId, UUID groupId) {
		AgentEntity agent = agentRepository.findById(agentId).orElseThrow(new Supplier<RuntimeException>() {
			@Override
			public RuntimeException get() {
				return new RuntimeException("Agent not found");
			}
		});
		AgentGroupEntity group = groupRepository.findById(groupId).orElseThrow(new Supplier<RuntimeException>() {
			@Override
			public RuntimeException get() {
				return new RuntimeException("Group not found");
			}
		});
// Buscamos ambos objetos. Si alguno falla, se cancela la operaci�n.
		agent.addGroup(group);
		agentRepository.save(agent);// A�adimos el grupo y persistimos el cambio
	}

// Desvincula un agente de un grupo espec�fico.
	@Transactional
	void removeGroup(UUID agentId, UUID groupId) {
		AgentEntity agent = agentRepository.findById(agentId).orElseThrow(new Supplier<RuntimeException>() {
			@Override
			public RuntimeException get() {
				return new RuntimeException("Agent not found");
			}
		});
		AgentGroupEntity group = groupRepository.findById(groupId).orElseThrow(new Supplier<RuntimeException>() {
			@Override
			public RuntimeException get() {
				return new RuntimeException("Group not found");
			}
		});
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

		AgentEntity agent = agentRepository.findById(agentId).orElseThrow(new Supplier<RuntimeException>() {
			@Override
			public RuntimeException get() {
				return new RuntimeException("Agent not found");
			}
		});
		agent.setEnabled(true);
		return agentMapper.toDTO(agentRepository.save(agent));
	}

// Desactiva un agente restringiendo sus operaciones.
	@Transactional
	public AgentDTO disable(UUID agentId) {

		AgentEntity agent = agentRepository.findById(agentId).orElseThrow(new Supplier<RuntimeException>() {
			@Override
			public RuntimeException get() {
				return new RuntimeException("Agent not found");
			}
		});
		agent.setEnabled(false);
		return agentMapper.toDTO(agentRepository.save(agent));
	}

}
