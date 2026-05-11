package io.github.styxiner.complyx_api.risk_modeling;

import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.github.styxiner.complyx_api.agents.AgentDTO;
import io.github.styxiner.complyx_api.agents.AgentEntity;
import io.github.styxiner.complyx_api.agents.AgentGroupEntity;



@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RiskMapper {

    ThreatDTO toThreatDTO(ThreatEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateThreatFromDTO(ThreatUpdateDTO dto, @MappingTarget ThreatEntity entity);

    @Mapping(source = "threat.name", target = "threatName")
    @Mapping(source = "agent.hostname", target = "agentHostname")
    @Mapping(source = "agent.groups", target = "groups", qualifiedByName = "extractGroupNames")
    RiskDTO toRiskDTO(RiskEntity entity);

    @Mapping(source = "groups", target = "groups", qualifiedByName = "extractGroupNames")
    AgentDTO toAgentDTO(AgentEntity entity);

    RiskDetailDTO toRiskDetailDTO(RiskEntity entity);

    List<RiskDTO> toRiskDTOList(List<RiskEntity> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRiskFromDTO(RiskUpdateDTO dto, @MappingTarget RiskEntity entity);

    @Named("extractGroupNames")
    default List<String> extractGroupNames(Set<AgentGroupEntity> groups) {
        if (groups == null) {
            return null;
        }

        List<String> names = new ArrayList<>();

        for (AgentGroupEntity group : groups) {
            names.add(group.getName());
        }

        return names;
    }}