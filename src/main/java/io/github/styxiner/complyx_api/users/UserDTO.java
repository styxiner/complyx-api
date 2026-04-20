package io.github.styxiner.complyx_api.users;

import java.util.Set;
import java.util.UUID;

public class UserDTO {
	private UUID id;
	private String username;
	private String email;
	private Set<String> roles;
}
