package io.github.styxiner.complyx_api.agents;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

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
		this.agentRepository = agentRepository;
		this.groupRepository = groupRepository;
		this.agentMapper = agentMapper;
	}

	// Ejecuta la busqueda con filtros dinamicos y convierte cada entidad a DTO.
	public Page<AgentDTO> findAll(AgentFilter filter, Pageable pageable) {
		Specification<AgentEntity> spec = AgentSpecifications.build(filter);
		return agentRepository.findAll(spec, pageable).map(new Function<AgentEntity, AgentDTO>() {
			@Override
			public AgentDTO apply(AgentEntity agent) {
				return agentMapper.toDTO(agent);
			}
		});
	}

	public AgentDTO findById(UUID agentId) {
		AgentEntity agent = agentRepository.findById(agentId).orElseThrow(new Supplier<RuntimeException>() {
			@Override
			public RuntimeException get() {
				return new RuntimeException("Agent not found");
			}
		});

		return agentMapper.toDTO(agent);
	}

	// Registra un nuevo agente asegurando que la IP no este repetida.
	@Transactional
	public AgentDTO register(AgentRegisterDTO agentRegisterDTO) {
		if (agentRepository.existsByIp(agentRegisterDTO.getIp())) {
			throw new RuntimeException("Agent with this IP already exists");
		}

		AgentEntity agent = agentMapper.toEntity(agentRegisterDTO);
		AgentEntity saved = agentRepository.save(agent);
		return agentMapper.toDTO(saved);
	}

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

		agent.addGroup(group);
		agentRepository.save(agent);
	}

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

	@Transactional
	public void delete(UUID agentId) {
		if (!agentRepository.existsById(agentId)) {
			throw new RuntimeException("Agent not found");
		}

		agentRepository.deleteById(agentId);
	}

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

	// Desactiva el agente sin borrarlo para conservar el registro.
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