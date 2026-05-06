package io.github.styxiner.complyx_api.agents;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.*;
// Construye filtros din�micos para AgentGroupEntity

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

// Construye filtros din�micos para AgentGroupEntity
public final class AgentGroupSpecifications {
	//Filtra por nombre (case insensitive)
	public static Specification<AgentGroupEntity> hasName(String name) {
		return new Specification<AgentGroupEntity>() {
			@Override
			public Predicate toPredicate(Root<AgentGroupEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (name == null) {
					return cb.conjunction();
				}
				return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
			}
		};
	}
	public static Specification<AgentGroupEntity> hasDescription(String description) {
		return new Specification<AgentGroupEntity>() {
			@Override
			public Predicate toPredicate(Root<AgentGroupEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (description == null || description.isBlank()) {
					return cb.conjunction();
				}
				return cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%");
			}
		};
	}

	//Filtra grupos que contienen un agente concreto
	public static Specification<AgentGroupEntity> hasAgent(UUID agentId) {
		 return new Specification<AgentGroupEntity>() {
	            @Override
	            public Predicate toPredicate(Root<AgentGroupEntity> root,
	                                         CriteriaQuery<?> query,
	                                         CriteriaBuilder cb) {
	                if (agentId == null) {
	                    return cb.conjunction();
	                }
	                // JOIN con la tabla de agentes
	                Join<Object, Object> agentJoin = root.join("agents", JoinType.INNER);
	                //Evita duplicados en resultados pues un grupo puede tener varios agentes
	                query.distinct(true);
	                return cb.equal(agentJoin.get("id"), agentId);
	            }
	        };
	}

	// combinaci�n filtros din�micos
	public static Specification<AgentGroupEntity> build(AgentGroupFilter filter) {
		if (filter == null) {
			return Specification.unrestricted();
		}
		Specification<AgentGroupEntity> spec = Specification.unrestricted();

		if (filter.getName() != null) {
			spec = spec.and(hasName(filter.getName()));
		}
		if (filter.getDescription() != null && !filter.getDescription().isBlank()) {
			spec = spec.and(hasDescription(filter.getDescription()));
		}

		if (filter.getAgentId() != null) {
			spec = spec.and(hasAgent(filter.getAgentId()));
		}
		return spec;
	}
}