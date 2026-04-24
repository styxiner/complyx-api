package io.github.styxiner.complyx_api.users;

import java.util.Set;
import java.util.UUID;

public class UserCreateDTO {
	private String username;
	private String email;
	private String password;
	private Set<UUID> roleIds;
	public UserCreateDTO(String username, String email, String password, Set<UUID> roleIds) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.roleIds = roleIds;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Set<UUID> getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(Set<UUID> roleIds) {
		this.roleIds = roleIds;
	}
	
	
	
	
}
