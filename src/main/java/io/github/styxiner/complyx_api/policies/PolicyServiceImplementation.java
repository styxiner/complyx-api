package io.github.styxiner.complyx_api.policies;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

@Service
@Transactional
public class PolicyServiceImplementation implements PolicyService {

    private final PolicyRepository policyRepository;
    private final PolicyMapper policyMapper;
    private final PolicyMapperImplementation policyMapperImplementation;
    private final AgentRepository agentRepository;
    private final AgentGroupRepository agentGroupRepository;

    public PolicyServiceImplementation(PolicyRepository policyRepository, PolicyMapper policyMapper,
            PolicyMapperImplementation policyMapperImplementation, AgentRepository agentRepository,
            AgentGroupRepository agentGroupRepository) {
        super();
        this.policyRepository = policyRepository;
        this.policyMapper = policyMapper;
        this.policyMapperImplementation = policyMapperImplementation;
        this.agentRepository = agentRepository;
        this.agentGroupRepository = agentGroupRepository;
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
        PolicyEntity entity = policyMapperImplementation.toEntity(dto, new HashMap<>());
        PolicyEntity saved = policyRepository.save(entity);
        return policyMapper.toDetailDTO(saved);
    }


    @Override
    public PolicyDetailDTO updatePolicy(UUID id, PolicyUpdateDTO dto) {
        Optional<PolicyEntity> opt = policyRepository.findById(id);
        if (!opt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Política no encontrada: " + id);
        }
        PolicyEntity entity = opt.get();
        policyMapperImplementation.updateEntity(dto, entity, new HashMap<>());
        PolicyEntity saved = policyRepository.save(entity);
        return policyMapper.toDetailDTO(saved);
    }


    @Override
    public void deletePolicy(UUID policyId) {
        Optional<PolicyEntity> opt = policyRepository.findById(policyId);
        if (!opt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Política no encontrada: " + policyId);
        }
        PolicyEntity policy = opt.get();

        /*
         * ManyToMany: limpiamos relaciones explícitamente en memoria
         * antes de borrar para evitar inconsistencias.
         */
        policy.getAgents().clear();
        policy.getGroups().clear();

        /*
         * CascadeType.ALL + orphanRemoval=true gestionan:
         * - elements, checks, remediations
         */
        policyRepository.delete(policy);
    }

    @Override
    public void assignToAgent(UUID policyId, UUID agentId) {
        PolicyEntity policy = requirePolicy(policyId);
        AgentEntity agent = requireAgent(agentId);

        if (!policy.getAgents().contains(agent)) {
            policy.addAgent(agent);
        }
        policyRepository.save(policy);
    }

    @Override
    public void unAssignToAgent(UUID policyId, UUID agentId) {
        PolicyEntity policy = requirePolicy(policyId);
        AgentEntity agent = requireAgent(agentId);

        if (policy.getAgents().contains(agent)) {
            policy.removeAgent(agent);
        }
        policyRepository.save(policy);
    }

    @Override
    public void assignToGroup(UUID policyId, UUID groupId) {
        PolicyEntity policy = requirePolicy(policyId);
        AgentGroupEntity group = requireGroup(groupId);

        if (!policy.getGroups().contains(group)) {
            policy.addGroup(group);
        }
        policyRepository.save(policy);
    }

    @Override
    public void unAssignToGroup(UUID policyId, UUID groupId) {
        PolicyEntity policy = requirePolicy(policyId);
        AgentGroupEntity group = requireGroup(groupId);

        if (policy.getGroups().contains(group)) {
            policy.removeGroup(group);
        }
        policyRepository.save(policy);
    }



    private PolicyEntity requirePolicy(UUID policyId) {
        Optional<PolicyEntity> opt = policyRepository.findById(policyId);
        if (!opt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Política no encontrada: " + policyId);
        }
        return opt.get();
    }

    private AgentEntity requireAgent(UUID agentId) {
        Optional<AgentEntity> opt = agentRepository.findById(agentId);
        if (!opt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Agente no encontrado: " + agentId);
        }
        return opt.get();
    }

    private AgentGroupEntity requireGroup(UUID groupId) {
        Optional<AgentGroupEntity> opt = agentGroupRepository.findById(groupId);
        if (!opt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Grupo no encontrado: " + groupId);
        }
        return opt.get();
    }
}