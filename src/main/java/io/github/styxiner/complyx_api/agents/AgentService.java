package io.github.styxiner.complyx_api.agents;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import io.github.styxiner.complyx_api.policies.PolicyCheckEntity;
import io.github.styxiner.complyx_api.policies.PolicyComplianceDTO;
import io.github.styxiner.complyx_api.policies.PolicyElementEntity;
import io.github.styxiner.complyx_api.policies.PolicyEntity;
import io.github.styxiner.complyx_api.policies.PolicyRepository;

@Service
@Transactional(readOnly = true)
public class AgentService {

    private final AgentRepository           agentRepository;
    private final AgentGroupRepository      groupRepository;
    private final AgentMapper               agentMapper;
    private final PolicyRepository          policyRepository;
    private final CheckResultRepository     checkResultRepository;
    private final ComplianceScoreRepository scoreRepository;

    public AgentService(
            AgentRepository agentRepository,
            AgentGroupRepository groupRepository,
            AgentMapper agentMapper,
            PolicyRepository policyRepository,
            CheckResultRepository checkResultRepository,
            ComplianceScoreRepository scoreRepository) {
        this.agentRepository      = agentRepository;
        this.groupRepository      = groupRepository;
        this.agentMapper          = agentMapper;
        this.policyRepository     = policyRepository;
        this.checkResultRepository = checkResultRepository;
        this.scoreRepository      = scoreRepository;
    }

    // ── Consultas ─────────────────────────────────────────────────────────────

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

    // ── Escritura ─────────────────────────────────────────────────────────────

    @Transactional
    public AgentDTO register(AgentRegisterDTO agentRegisterDTO) {
        if (agentRepository.existsByIp(agentRegisterDTO.getIp())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe un agente con la IP: " + agentRegisterDTO.getIp());
        }
        AgentEntity agent = agentMapper.toEntity(agentRegisterDTO);
        return agentMapper.toDTO(agentRepository.save(agent));
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

    @Transactional
    public AgentDTO disable(UUID agentId) {
        AgentEntity agent = requireAgent(agentId);
        agent.setEnabled(false);
        return agentMapper.toDTO(agentRepository.save(agent));
    }

    // ── Resultados de cumplimiento ────────────────────────────────────────────

    /**
     * Construye el DTO de cumplimiento cruzando:
     *   - policy_elements + policy_checks  (definición)
     *   - check_results DISTINCT ON check_id (último resultado por check)
     *   - compliance_scores                  (score por elemento)
     */
    public PolicyComplianceDTO getPolicyResults(UUID agentId, UUID policyId) {

        // Validar que el agente existe
        requireAgent(agentId);

        // Cargar la política con sus elementos y checks
        PolicyEntity policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Política no encontrada: " + policyId));

        // Último resultado por check (una sola query con DISTINCT ON)
        List<CheckResultEntity> rawResults =
                checkResultRepository.findLatestByAgentAndPolicy(agentId, policyId);

        Map<UUID, CheckResultEntity> resultByCheckId = rawResults.stream()
                .collect(Collectors.toMap(
                        r -> r.getCheck().getId(),
                        r -> r));

        // Scores por elemento
        List<ComplianceScoreEntity> scores =
                scoreRepository.findByAgentIdAndPolicy_Id(agentId, policyId);

        Map<UUID, ComplianceScoreEntity> scoreByElementId = scores.stream()
                .collect(Collectors.toMap(
                        ComplianceScoreEntity::getPolicyElementId,
                        s -> s));

        // ── Construir DTO ─────────────────────────────────────────────────────

        PolicyComplianceDTO dto = new PolicyComplianceDTO();
        dto.setPolicyId(policy.getId());
        dto.setPolicyName(policy.getName());
        dto.setPolicyVersion(policy.getVersion());
        dto.setSeverity(policy.getSeverity() != null ? policy.getSeverity().name() : null);

        int totalChecks  = 0;
        int passedChecks = 0;

        List<PolicyComplianceDTO.ElementComplianceDTO> elementDTOs = new ArrayList<>();

        for (PolicyElementEntity element : policy.getElements()) {

            PolicyComplianceDTO.ElementComplianceDTO elDTO =
                    new PolicyComplianceDTO.ElementComplianceDTO();
            elDTO.setElementId(element.getId());
            elDTO.setElementName(element.getName());

            ComplianceScoreEntity score = scoreByElementId.get(element.getId());
            if (score != null) {
                elDTO.setTotalChecks(score.getTotalChecks());
                elDTO.setPassedChecks(score.getPassedChecks());
                elDTO.setScore(score.getScore());
                elDTO.setLastUpdated(score.getLastUpdated());
            }

            List<PolicyComplianceDTO.CheckComplianceDTO> checkDTOs = new ArrayList<>();

            for (PolicyCheckEntity check : element.getChecks()) {

                PolicyComplianceDTO.CheckComplianceDTO chDTO =
                        new PolicyComplianceDTO.CheckComplianceDTO();
                chDTO.setCheckId(check.getId());
                chDTO.setCheckName(check.getName());
                chDTO.setRationale(check.getRationale());

                CheckResultEntity result = resultByCheckId.get(check.getId());
                if (result != null) {
                    chDTO.setPassed(result.isPassed());
                    chDTO.setDetail(result.getDetail());
                    chDTO.setActualValue(result.getActualValue());
                    chDTO.setExpectedValue(result.getExpectedValue());
                    chDTO.setExecutedAt(result.getExecutedAt());
                    totalChecks++;
                    if (result.isPassed()) passedChecks++;
                }

                checkDTOs.add(chDTO);
            }

            elDTO.setChecks(checkDTOs);
            elementDTOs.add(elDTO);
        }

        dto.setElements(elementDTOs);
        dto.setTotalChecks(totalChecks);
        dto.setPassedChecks(passedChecks);

        double globalScore = scores.stream()
                .mapToDouble(ComplianceScoreEntity::getScore)
                .average()
                .orElse(0.0);
        dto.setGlobalScore(Math.round(globalScore * 10.0) / 10.0);

        return dto;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

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