package io.github.styxiner.complyx_api.agents;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

// Construye filtros dinámicos para AgentGroupEntity

public class AgentGroupSpecifications {

//Filtra por nombre (case insensitive)
	public static Specification<AgentGroupEntity> hasName(String name) {
		return (root, query, cb) -> name == null ? null
				: cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
	}

//Filtra grupos que contienen un agente concreto
	public static Specification<AgentGroupEntity> hasAgent(UUID agentId) {
		return (root, query, cb) -> {
			if (agentId == null)
				return null;
			Join<Object, Object> agentJoin = root.join("agents", JoinType.INNER);// JOIN con la tabla de agentes
			query.distinct(true);// Evita duplicados en resultados pues un grupo puede tener varios agentes
			return cb.equal(agentJoin.get("id"), agentId);
		};
	}

	// combinación filtros dinámicos
	public static Specification<AgentGroupEntity> build(AgentGroupFilter filter) {
		if (filter == null) {
			return Specification.unrestricted();
		}
		Specification<AgentGroupEntity> spec = Specification.unrestricted();

		if (filter.getName() != null) {
			spec = spec.and(hasName(filter.getName()));
		}

		if (filter.getAgentId() != null) {
			spec = spec.and(hasAgent(filter.getAgentId()));
		}
		return spec;
	}
}