package io.github.styxiner.complyx_api.users;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
	private String username;
	private String email;
	private String passwordHash;
	private Set<RoleEntity> roles;
}
