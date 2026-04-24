package io.github.styxiner.complyx_api.users;

import org.springframework.data.jpa.domain.Specification;

public class RoleSpecifications {
	public static Specification<RoleEntity> build(RoleFilter filter) {
		Specification<RoleEntity> spec = new RolenameSpecification(filter.getRoleName());
		
		return spec;

	}
}
