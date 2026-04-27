package io.github.styxiner.complyx_api.users;

import java.util.Set;
import java.util.UUID;

public class UserDTO {
	private UUID id;
	private String username;
	private String email;
	private Set<String> roles;

	public UserDTO(UUID id, String username, String email, Set<String> roles) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Set<String> getRoles() {
		return roles;
	}
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	
	
}
