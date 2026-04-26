package io.github.styxiner.complyx_api.users;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;

@ParameterObject
public class UserFilter {
	private String username;
	private String email;
	private UUID roleId;
	public String getUsername() {
		return username;
	}
	public String getEmail() {
		return email;
	}
	public UUID getRoleId() {
		return roleId;
	}
}
