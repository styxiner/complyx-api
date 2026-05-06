package io.github.styxiner.complyx_api.agents;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class AgentSpecifications {

	public static Specification<AgentEntity> hasIp(String ip) {
		return new Specification<AgentEntity>() {
			@Override
			public Predicate toPredicate(Root<AgentEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (ip == null || ip.isBlank()) {
					return cb.conjunction();
				}
				return cb.equal(root.get("ip"), ip);
			}
		};
	}

	public static Specification<AgentEntity> hasHostname(String hostname) {
		return new Specification<AgentEntity>() {
			@Override
			public Predicate toPredicate(Root<AgentEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (hostname == null || hostname.isBlank()) {
					return cb.conjunction();
				}
				return cb.like(cb.lower(root.get("hostname")), "%" + hostname.toLowerCase() + "%");
			}
		};
	}

	public static Specification<AgentEntity> hasOsName(String osName) {
		return new Specification<AgentEntity>() {
			@Override
			public Predicate toPredicate(Root<AgentEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (osName == null || osName.isBlank()) {
					return cb.conjunction();
				}
				return cb.like(cb.lower(root.get("osName")), "%" + osName.toLowerCase() + "%");
			}
		};
	}

	public static Specification<AgentEntity> isEnabled(Boolean enabled) {
		return new Specification<AgentEntity>() {
			@Override
			public Predicate toPredicate(Root<AgentEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (enabled == null) {
					return cb.conjunction();
				}
				return cb.equal(root.get("enabled"), enabled);
			}
		};
	}

	public static Specification<AgentEntity> inGroup(UUID groupId) {
		return new Specification<AgentEntity>() {
			@Override
			public Predicate toPredicate(Root<AgentEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (groupId == null) {
					return cb.conjunction();
				}
				query.distinct(true);
				Join<Object, Object> groupJoin = root.join("groups", JoinType.INNER);
				return cb.equal(groupJoin.get("id"), groupId);
			}
		};
	}

	// Construye la Specification final combinando unicamente los filtros informados.
	public static Specification<AgentEntity> build(AgentFilter filter) {
		if (filter == null) {
			return Specification.unrestricted();
		}

		Specification<AgentEntity> spec = Specification.unrestricted();

		if (filter.getIp() != null && !filter.getIp().isBlank()) {
			spec = spec.and(hasIp(filter.getIp()));
		}

		if (filter.getHostname() != null && !filter.getHostname().isBlank()) {
			spec = spec.and(hasHostname(filter.getHostname()));
		}

		if (filter.getOsName() != null && !filter.getOsName().isBlank()) {
			spec = spec.and(hasOsName(filter.getOsName()));
		}

		if (filter.getEnabled() != null) {
			spec = spec.and(isEnabled(filter.getEnabled()));
		}

		if (filter.getGroupId() != null) {
			spec = spec.and(inGroup(filter.getGroupId()));
		}

		return spec;
	}
}