package io.github.styxiner.complyx_api.agents;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    // Ejecuta la búsqueda con filtros dinámicos y convierte cada entidad a DTO.
    public Page<AgentDTO> findAll(AgentFilter filter, Pageable pageable) {
        Specification<AgentEntity> spec = AgentSpecifications.build(filter);
        return agentRepository.findAll(spec, pageable)
                .map(new java.util.function.Function<AgentEntity, AgentDTO>() {
                    @Override
                    public AgentDTO apply(AgentEntity agent) {
                        return agentMapper.toDTO(agent);
                    }
                });
    }

    public AgentDTO findById(UUID agentId) {
        return agentMapper.toDTO(requireAgent(agentId));
    }

    // Registra un nuevo agente asegurando que la IP no esté repetida.
    @Transactional
    public AgentDTO register(AgentRegisterDTO agentRegisterDTO) {
        if (agentRepository.existsByIp(agentRegisterDTO.getIp())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe un agente con la IP: " + agentRegisterDTO.getIp());
        }
        AgentEntity agent = agentMapper.toEntity(agentRegisterDTO);
        AgentEntity saved = agentRepository.save(agent);
        return agentMapper.toDTO(saved);
    }

    @Transactional
    void assignGroup(UUID agentId, UUID groupId) {
        AgentEntity agent = requireAgent(agentId);
        AgentGroupEntity group = requireGroup(groupId);
        agent.addGroup(group);
        agentRepository.save(agent);
    }

    @Transactional
    void removeGroup(UUID agentId, UUID groupId) {
        AgentEntity agent = requireAgent(agentId);
        AgentGroupEntity group = requireGroup(groupId);
        agent.removeGroup(group);
        agentRepository.save(agent);
    }

    @Transactional
    public void delete(UUID agentId) {
        if (!agentRepository.existsById(agentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Agente no encontrado: " + agentId);
        }
        agentRepository.deleteById(agentId);
    }

    @Transactional
    public AgentDTO enable(UUID agentId) {
        AgentEntity agent = requireAgent(agentId);
        agent.setEnabled(true);
        return agentMapper.toDTO(agentRepository.save(agent));
    }

    // Desactiva el agente sin borrarlo para conservar el registro.
    @Transactional
    public AgentDTO disable(UUID agentId) {
        AgentEntity agent = requireAgent(agentId);
        agent.setEnabled(false);
        return agentMapper.toDTO(agentRepository.save(agent));
    }

    // -------------------------------------------------------------------------
    // Helpers privados
    // -------------------------------------------------------------------------

    private AgentEntity requireAgent(UUID agentId) {
        Optional<AgentEntity> opt = agentRepository.findById(agentId);
        if (!opt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Agente no encontrado: " + agentId);
        }
        return opt.get();
    }

    private AgentGroupEntity requireGroup(UUID groupId) {
        Optional<AgentGroupEntity> opt = groupRepository.findById(groupId);
        if (!opt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Grupo no encontrado: " + groupId);
        }
        return opt.get();
    }
}