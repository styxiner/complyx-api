package io.github.styxiner.complyx_api.users;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {
	Specification<UserEntity> hasUsername(String name) {return null;}
	Specification<UserEntity> hasEmail(String email) {return null;}
	Specification<UserEntity> hasRole(UUID roleId) {return null;}
	Specification<UserEntity> build(UserFilter filter) {return null;}
}
