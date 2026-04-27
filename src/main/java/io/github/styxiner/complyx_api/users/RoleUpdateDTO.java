package io.github.styxiner.complyx_api.users;

import java.util.UUID;

public class RoleUpdateDTO {
	private UUID id;
	private String rolename;
	public RoleUpdateDTO(UUID id, String rolename) {
		super();
		this.id = id;
		this.rolename = rolename;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	
	

}
