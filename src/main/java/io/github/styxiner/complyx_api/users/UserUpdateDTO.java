package io.github.styxiner.complyx_api.users;

import java.util.UUID;

public class UserUpdateDTO {
	private UUID id;
	private String email;
	private String username;
	private String password;
	
	public UserUpdateDTO(UUID id, String username, String email, String password) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
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

	public void setUsernamel(String username) {
		this.username= username;
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
}
