package io.github.styxiner.complyx_api.users;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class UsernameSpecification implements Specification<UserEntity>{
	private static final long serialVersionUID = 1L;
	private final String USERNAME;	

	public UsernameSpecification(String username) {
		this.USERNAME = username;
	}	

	@Override
	public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		if (USERNAME == null || USERNAME.isBlank()) {
			return criteriaBuilder.conjunction();
		}
		
		return criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + USERNAME.toLowerCase() + "%");
	}
}
