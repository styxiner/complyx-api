package io.github.styxiner.complyx_api.users;

import java.util.Set;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCreateDTO {
	private String username;
	private String email;
	private String password;
	private Set<UUID> roleIds;
}
