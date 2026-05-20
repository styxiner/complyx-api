package io.github.styxiner.complyx_api.policies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import io.github.styxiner.complyx_api.agents.AgentEntity;
import io.github.styxiner.complyx_api.agents.AgentGroupEntity;
import io.github.styxiner.complyx_api.agents.AgentGroupRepository;
import io.github.styxiner.complyx_api.agents.AgentRepository;
import io.github.styxiner.complyx_api.regulations.RegulationSectionEntity;
import io.github.styxiner.complyx_api.regulations.RegSectionRepository;

@Service
@Transactional
public class PolicyServiceImplementation implements PolicyService {

    private final PolicyRepository policyRepository;
    private final PolicyMapper policyMapper;
    private final PolicyMapperImplementation policyMapperImplementation;
    private final AgentRepository agentRepository;
    private final AgentGroupRepository agentGroupRepository;
    private final RegSectionRepository regulationSectionRepository;

    public PolicyServiceImplementation(PolicyRepository policyRepository, PolicyMapper policyMapper,
            PolicyMapperImplementation policyMapperImplementation, AgentRepository agentRepository,
            AgentGroupRepository agentGroupRepository,
            RegSectionRepository regulationSectionRepository) {
        super();
        this.policyRepository = policyRepository;
        this.policyMapper = policyMapper;
        this.policyMapperImplementation = policyMapperImplementation;
        this.agentRepository = agentRepository;
        this.agentGroupRepository = agentGroupRepository;
        this.regulationSectionRepository = regulationSectionRepository; 
    }

    @Override
    @Transactional(readOnly = true)
    public PolicyDetailDTO getPolicyById(UUID id) {
        Optional<PolicyEntity> opt = policyRepository.findById(id);
        if (!opt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Política no encontrada: " + id);
        }
        return policyMapper.toDetailDTO(opt.get());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicySummaryDTO> getPoliciesByAgent(UUID agentId) {
        List<PolicyEntity> policies = policyRepository.findAll(PolicySpecifications.assignedToAgent(agentId));
        return policyMapper.toSummaryList(policies);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PolicySummaryDTO> getAllPolicies(PolicyFilter filter, Pageable pageable) {
        Page<PolicyEntity> page = policyRepository.findAll(PolicySpecifications.build(filter), pageable);
        return page.map(new java.util.function.Function<PolicyEntity, PolicySummaryDTO>() {
            @Override
            public PolicySummaryDTO apply(PolicyEntity entity) {
                return policyMapper.toSummaryDTO(entity);
            }
        });
    }

    @Override
    public PolicyDetailDTO createPolicy(PolicyCreateDTO dto) {
        // Construir el mapa de secciones referenciadas en el DTO 
        Map<UUID, RegulationSectionEntity> regulationMap = buildRegulationMap(dto);

        PolicyEntity entity = policyMapperImplementation.toEntity(dto, regulationMap);
        PolicyEntity saved  = policyRepository.save(entity);
        return policyMapper.toDetailDTO(saved);
    }

    @Override
    public PolicyDetailDTO updatePolicy(UUID id, PolicyUpdateDTO dto) {
        Optional<PolicyEntity> opt = policyRepository.findById(id);
        if (!opt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Política no encontrada: " + id);
        }

        // Construir el mapa de secciones referenciadas en el DTO
        Map<UUID, RegulationSectionEntity> regulationMap = buildRegulationMapForUpdate(dto);

        PolicyEntity entity = opt.get();
        policyMapperImplementation.updateEntity(dto, entity, regulationMap);
        PolicyEntity saved = policyRepository.save(entity);
        return policyMapper.toDetailDTO(saved);
    }


    /*
     * Recoge todos los regulationSectionIds del CreateDTO,
     * los busca en BD en una sola query y devuelve el mapa.
     */
    private Map<UUID, RegulationSectionEntity> buildRegulationMap(PolicyCreateDTO dto) {
        if (dto.getElements() == null) return new HashMap<>();

        Set<UUID> ids = dto.getElements().stream()
            .filter(el -> el.getChecks() != null)
            .flatMap(el -> el.getChecks().stream())
            .filter(ch -> ch.getRegulationSectionIds() != null)
            .flatMap(ch -> ch.getRegulationSectionIds().stream())
            .collect(Collectors.toSet());

        if (ids.isEmpty()) return new HashMap<>();

        return regulationSectionRepository.findAllById(ids).stream()
            .collect(Collectors.toMap(RegulationSectionEntity::getId, s -> s));
    }

    /**
     * Versión para UpdateDTO misma lógica adaptada a los tipos Update.
     */
    private Map<UUID, RegulationSectionEntity> buildRegulationMapForUpdate(PolicyUpdateDTO dto) {
        if (dto.getElements() == null) return new HashMap<>();

        Set<UUID> ids = dto.getElements().stream()
            .filter(el -> el.getChecks() != null)
            .flatMap(el -> el.getChecks().stream())
            .filter(ch -> ch.getRegulationSectionIds() != null)
            .flatMap(ch -> ch.getRegulationSectionIds().stream())
            .collect(Collectors.toSet());

        if (ids.isEmpty()) return new HashMap<>();

        return regulationSectionRepository.findAllById(ids).stream()
            .collect(Collectors.toMap(RegulationSectionEntity::getId, s -> s));
    }


    @Override
    public void deletePolicy(UUID policyId) {
        Optional<PolicyEntity> opt = policyRepository.findById(policyId);
        if (!opt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Política no encontrada: " + policyId);
        }
        PolicyEntity policy = opt.get();
        policy.getAgents().clear();
        policy.getGroups().clear();
        policyRepository.delete(policy);
    }

    @Override
    public void assignToAgent(UUID policyId, UUID agentId) {
        PolicyEntity policy = requirePolicy(policyId);
        AgentEntity agent   = requireAgent(agentId);
        if (!policy.getAgents().contains(agent)) policy.addAgent(agent);
        policyRepository.save(policy);
    }

    @Override
    public void unAssignToAgent(UUID policyId, UUID agentId) {
        PolicyEntity policy = requirePolicy(policyId);
        AgentEntity agent   = requireAgent(agentId);
        if (policy.getAgents().contains(agent)) policy.removeAgent(agent);
        policyRepository.save(policy);
    }

    @Override
    public void assignToGroup(UUID policyId, UUID groupId) {
        PolicyEntity policy     = requirePolicy(policyId);
        AgentGroupEntity group  = requireGroup(groupId);
        if (!policy.getGroups().contains(group)) policy.addGroup(group);
        policyRepository.save(policy);
    }

    @Override
    public void unAssignToGroup(UUID policyId, UUID groupId) {
        PolicyEntity policy     = requirePolicy(policyId);
        AgentGroupEntity group  = requireGroup(groupId);
        if (policy.getGroups().contains(group)) policy.removeGroup(group);
        policyRepository.save(policy);
    }

    private PolicyEntity requirePolicy(UUID policyId) {
        return policyRepository.findById(policyId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Política no encontrada: " + policyId));
    }

    private AgentEntity requireAgent(UUID agentId) {
        return agentRepository.findById(agentId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agente no encontrado: " + agentId));
    }

    private AgentGroupEntity requireGroup(UUID groupId) {
        return agentGroupRepository.findById(groupId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grupo no encontrado: " + groupId));
    }
}