package io.github.styxiner.complyx_api.users;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class RoleEntity {
	
	@Id
	@GeneratedValue
	private UUID id;
	
	@Column(name = "rolename", unique = true, nullable = false)
	private String roleName;
	
	public RoleEntity() {
	}
	
	public RoleEntity(String roleName) {
		this.roleName = roleName;
	}

	public RoleEntity(UUID id, String roleName) {
		this.id = id;
		this.roleName = roleName;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	
	
	
}
