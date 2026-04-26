package io.github.styxiner.complyx_api.users;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class RolenameSpecification implements Specification<RoleEntity> {

	private final String ROLENAME;	
	
	public RolenameSpecification(String rolename) {
		this.ROLENAME = rolename;
	}
	
	@Override
	public Predicate toPredicate(Root<RoleEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		if (ROLENAME == null || ROLENAME.isBlank()) {
			return criteriaBuilder.conjunction();
		}
		
		return criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + ROLENAME.toLowerCase() + "%");
	}

}
