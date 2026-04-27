package io.github.styxiner.complyx_api.users;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class EmailSpecification implements Specification<UserEntity>{
	private static final long serialVersionUID = 1L;
	private final String EMAIL;
	
	public EmailSpecification(String email) {
		this.EMAIL = email;
	}

	@Override
	public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		if (EMAIL == null || EMAIL.isBlank()) {
			return criteriaBuilder.conjunction();
		}
		
		return criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + EMAIL.toLowerCase() + "%");
	}	
}
