package io.github.styxiner.complyx_api.risk_modeling;

import io.github.styxiner.complyx_api.agents.AgentRepository;
import io.github.styxiner.complyx_api.policies.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RiskService {

    private final RiskRepository riskRepository;
    private final ThreatRepository threatRepository;
    private final AgentRepository agentRepository;
    private final PolicyRepository policyRepository;
    private final RiskMapper mapper;

    // ---------------------------------------------------------------------------
    // Consultas
    // ---------------------------------------------------------------------------

    public Page<RiskDTO> findAll(RiskFilter filter, Pageable pageable) {
        return riskRepository.findAll(RiskSpecifications.build(filter), pageable)
                .map(mapper::toRiskDTO);
    }

    public RiskDetailDTO findById(UUID riskId) {
        return riskRepository.findById(riskId)
                .map(mapper::toRiskDetailDTO)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Riesgo no encontrado: " + riskId));
    }

    // ---------------------------------------------------------------------------
    // Creación
    // ---------------------------------------------------------------------------

    @Transactional
    public RiskDTO create(RiskCreateDTO dto) {
        var threat = threatRepository.findById(dto.threatId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Amenaza no encontrada: " + dto.threatId()));

        var agent = agentRepository.findById(dto.agentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Agente no encontrado: " + dto.agentId()));

        RiskLevel level = computeRiskLevel(dto.impact(), dto.probability());

        RiskEntity risk = RiskEntity.builder()
                .threat(threat)
                .agent(agent)
                .impact(dto.impact())
                .probability(dto.probability())
                .riskLevel(level)
                .status(RiskStatus.OPEN)
                .build();

        return mapper.toRiskDTO(riskRepository.save(risk));
    }

    // ---------------------------------------------------------------------------
    // Actualización
    // ---------------------------------------------------------------------------

    @Transactional
    public RiskDTO update(UUID riskId, RiskUpdateDTO dto) {
        RiskEntity risk = requireRisk(riskId);
        requireNotClosed(risk);

        mapper.updateRiskFromDTO(dto, risk);

        // Recalcular nivel si cambian impacto o probabilidad
        if (dto.impact() != null || dto.probability() != null) {
            BigDecimal impact      = dto.impact()      != null ? dto.impact()      : risk.getImpact();
            BigDecimal probability = dto.probability() != null ? dto.probability() : risk.getProbability();
            risk.setRiskLevel(computeRiskLevel(impact, probability));
        }

        return mapper.toRiskDTO(riskRepository.save(risk));
    }

    // ---------------------------------------------------------------------------
    // Transiciones de estado
    // ---------------------------------------------------------------------------

    @Transactional
    public RiskDTO close(UUID riskId) {
        RiskEntity risk = requireRisk(riskId);
        requireNotClosed(risk);
        risk.setStatus(RiskStatus.CLOSED);
        return mapper.toRiskDTO(riskRepository.save(risk));
    }

    @Transactional
    public RiskDTO accept(UUID riskId) {
        RiskEntity risk = requireRisk(riskId);
        requireNotClosed(risk);
        risk.setStatus(RiskStatus.ACCEPTED);
        risk.setAcceptanceDate(LocalDateTime.now());
        return mapper.toRiskDTO(riskRepository.save(risk));
    }

    @Transactional
    public RiskDTO transfer(UUID riskId) {
        RiskEntity risk = requireRisk(riskId);
        requireNotClosed(risk);
        risk.setStatus(RiskStatus.TRANSFERRED);
        return mapper.toRiskDTO(riskRepository.save(risk));
    }

    @Transactional
    public RiskDTO setMonitoring(UUID riskId) {
        RiskEntity risk = requireRisk(riskId);
        requireNotClosed(risk);
        risk.setStatus(RiskStatus.MONITORING);
        return mapper.toRiskDTO(riskRepository.save(risk));
    }

    // ---------------------------------------------------------------------------
    // Políticas mitigadoras
    // ---------------------------------------------------------------------------

    @Transactional
    public void linkPolicy(UUID riskId, UUID policyId) {
        if (!riskRepository.existsById(riskId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Riesgo no encontrado: " + riskId);
        }
        if (!policyRepository.existsById(policyId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Política no encontrada: " + policyId);
        }
        riskRepository.linkPolicy(riskId, policyId);
    }

    @Transactional
    public void unlinkPolicy(UUID riskId, UUID policyId) {
        if (!riskRepository.existsById(riskId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Riesgo no encontrado: " + riskId);
        }
        riskRepository.unlinkPolicy(riskId, policyId);
    }

    // ---------------------------------------------------------------------------
    // Lógica de negocio: cálculo de nivel de riesgo
    // ---------------------------------------------------------------------------

    /**
     * Calcula el nivel de riesgo usando la fórmula impacto × probabilidad.
     *
     * Escala 0-10 en ambas dimensiones → producto máximo 100.
     *
     * | Producto  | Nivel    |
     * |-----------|----------|
     * | ≥ 70      | CRITICAL |
     * | 49 – 69   | HIGH     |
     * | 20 – 48   | MEDIUM   |
     * | < 20      | LOW      |
     */
    public RiskLevel computeRiskLevel(BigDecimal impact, BigDecimal probability) {
        if (impact == null || probability == null) return RiskLevel.LOW;
        return RiskLevel.fromScore(impact.doubleValue(), probability.doubleValue());
    }

    // ---------------------------------------------------------------------------
    // Helpers privados
    // ---------------------------------------------------------------------------

    private RiskEntity requireRisk(UUID riskId) {
        return riskRepository.findById(riskId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Riesgo no encontrado: " + riskId));
    }

    private void requireNotClosed(RiskEntity risk) {
        if (risk.getStatus() == RiskStatus.CLOSED) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "El riesgo " + risk.getId() + " ya está cerrado y no puede modificarse");
        }
    }
}