package io.github.styxiner.complyx_api.users;

public class RoleCreateDTO {
	private String roleName;

	public RoleCreateDTO(String roleName) {
		this.roleName = roleName;
	}

	public String getName() {
		return roleName;
	}

	public void setName(String name) {
		this.roleName = name;
	}
}
