package io.github.styxiner.complyx_api.risk_modeling;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RiskMapper {

    // ---------------------------------------------------------------------------
    // Threat
    // ---------------------------------------------------------------------------

    ThreatDTO toThreatDTO(ThreatEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateThreatFromDTO(ThreatUpdateDTO dto, @MappingTarget ThreatEntity entity);

    // ---------------------------------------------------------------------------
    // Risk
    // ---------------------------------------------------------------------------

    @Mapping(source = "threat.name",      target = "threatName")
    @Mapping(source = "agent.hostname",   target = "agentHostname")
    RiskDTO toRiskDTO(RiskEntity entity);

    @Mapping(source = "threat",  target = "threat")
    @Mapping(source = "agent",   target = "agent")
    @Mapping(source = "policies", target = "policies")
    RiskDetailDTO toRiskDetailDTO(RiskEntity entity);

    List<RiskDTO> toRiskDTOList(List<RiskEntity> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRiskFromDTO(RiskUpdateDTO dto, @MappingTarget RiskEntity entity);
}