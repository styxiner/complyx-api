package io.github.styxiner.complyx_api.policies;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/*
 * Construcción de queries dinámicas para PolicyEntity.
 * 
 */
public final class PolicySpecifications {

	private PolicySpecifications() {
	}

	public static Specification<PolicyEntity> hasName(String name) {
		return new Specification<PolicyEntity>() {
			@Override
			public Predicate toPredicate(Root<PolicyEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (name == null || name.isBlank()) {
					return cb.conjunction();
				}
				return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
			}
		};
	}

	public static Specification<PolicyEntity> hasSeverity(Severity severity) {
		return new Specification<PolicyEntity>() {
			@Override
			public Predicate toPredicate(Root<PolicyEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (severity == null) {
					return cb.conjunction();
				}
				return cb.equal(root.get("severity"), severity);
			}
		};
	}
	
	public static Specification<PolicyEntity> hasStatus(PolicyStatus status) {
	    return (root, query, cb) -> {
	        if (status == null) return cb.conjunction();
	        return cb.equal(root.get("status"), status);
	    };
	}

	public static Specification<PolicyEntity> assignedToAgent(UUID agentId) {
		return new Specification<PolicyEntity>() {
			@Override
			public Predicate toPredicate(Root<PolicyEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (agentId == null) {
					return cb.conjunction();
				}
				query.distinct(true);
				Join<Object, Object> agents = root.join("agents", JoinType.LEFT); // LEFT para poder combinar con unassigned
				return cb.equal(agents.get("id"), agentId);
			}
		};
	}

	public static Specification<PolicyEntity> assignedToGroup(UUID groupId) {
		return new Specification<PolicyEntity>() {
			@Override
			public Predicate toPredicate(Root<PolicyEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (groupId == null) {
					return cb.conjunction();
				}
				query.distinct(true);
				Join<Object, Object> groups = root.join("groups", JoinType.LEFT); // LEFT para poder combinar con unassigned
				return cb.equal(groups.get("id"), groupId);
			}
		};
	}
	public static Specification<PolicyEntity> byRegulationId(UUID regulationId) {
		return new Specification<PolicyEntity>() {
			@Override
			public Predicate toPredicate(Root<PolicyEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (regulationId == null) {
					return cb.conjunction();
				}
				query.distinct(true);
				// Hacemos el Join desde PolicyEntity -> elements -> checks -> regulationSections -> regulation
				Join<Object, Object> elements = root.join("elements");
				Join<Object, Object> checks = elements.join("checks");
				Join<Object, Object> regulationSections = checks.join("regulationSections");
				Join<Object, Object> regulation = regulationSections.join("regulation");
				return cb.equal(regulation.get("id"), regulationId);
			}
		};
	}

	public static Specification<PolicyEntity> isAssigned() {
		return new Specification<PolicyEntity>() {
			@Override
			public Predicate toPredicate(Root<PolicyEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				return cb.or(
						cb.isNotEmpty(root.get("agents")),
						cb.isNotEmpty(root.get("groups")));
			}
		};
	}

	public static Specification<PolicyEntity> isUnassigned() {
		return new Specification<PolicyEntity>() {
			@Override
			public Predicate toPredicate(Root<PolicyEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				return cb.and(
						cb.isEmpty(root.get("agents")),
						cb.isEmpty(root.get("groups")));
			}
		};
	}

	private static Specification<PolicyEntity> anyOf(
			final Specification<PolicyEntity> left,
			final Specification<PolicyEntity> right) {

		return new Specification<PolicyEntity>() {
			@Override
			public Predicate toPredicate(Root<PolicyEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate leftPredicate = left.toPredicate(root, query, cb);
				Predicate rightPredicate = right.toPredicate(root, query, cb);
				query.distinct(true);
				return cb.or(leftPredicate, rightPredicate);
			}
		};
	}

	public static Specification<PolicyEntity> build(PolicyFilter filter) {
		if (filter == null) {
			return Specification.unrestricted();
		}

		Specification<PolicyEntity> spec = Specification.unrestricted();
		
		if (filter.getStatus() != null) {
		    spec = spec.and(hasStatus(filter.getStatus()));
		}

		if (filter.getName() != null && !filter.getName().isBlank()) {
			spec = spec.and(hasName(filter.getName()));
		}

		if (filter.getSeverity() != null) {
			spec = spec.and(hasSeverity(filter.getSeverity()));
		}
		if (filter.getRegulationId() != null) {
			spec = spec.and(byRegulationId(filter.getRegulationId()));
		}
		Specification<PolicyEntity> assignmentSpec = Specification.unrestricted();
		boolean hasAssignmentFilters = false;

		if (filter.getAssignedToAgentId() != null) {
			assignmentSpec = assignmentSpec.and(assignedToAgent(filter.getAssignedToAgentId()));
			hasAssignmentFilters = true;
		}

		if (filter.getAssignedToGroupId() != null) {
			assignmentSpec = assignmentSpec.and(assignedToGroup(filter.getAssignedToGroupId()));
			hasAssignmentFilters = true;
		}

		if (Boolean.TRUE.equals(filter.getIncludeUnassigned())) {
			// Si hay filtros de asignación, incluye también las no asignadas.
			if (hasAssignmentFilters) {
				spec = spec.and(anyOf(assignmentSpec, isUnassigned()));
			}
			// Si no hay filtros de asignación, true equivale a no restringir.
		} else if (Boolean.FALSE.equals(filter.getIncludeUnassigned())) {
			// Si no se quieren las no asignadas y no hay filtros concretos,
			// mostramos solo las que tienen al menos una asignación.
			if (hasAssignmentFilters) {
				spec = spec.and(assignmentSpec);
			} else {
				spec = spec.and(isAssigned());
			}
		} else {
			// Comportamiento por defecto: solo aplicar filtros concretos si existen.
			if (hasAssignmentFilters) {
				spec = spec.and(assignmentSpec);
			}
		}

		return spec;
	}
}