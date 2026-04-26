package io.github.styxiner.complyx_api.users;

import org.springframework.data.jpa.domain.Specification;


// Como no les gustan los lambdas, se hace una especificación por cada campo.
public class UserSpecifications {
	public static Specification<UserEntity> build(UserFilter filter) {
		Specification<UserEntity> spec = new UsernameSpecification(filter.getUsername());
		spec = spec.and(new EmailSpecification(filter.getEmail()));
		spec = spec.and(new RoleSpecification(filter.getRoleId()));
		
		return spec;
	}
}
