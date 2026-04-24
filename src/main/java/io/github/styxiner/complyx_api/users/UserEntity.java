package io.github.styxiner.complyx_api.users;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {
	
	@Id
	@GeneratedValue
	private UUID id;
	
	@Column(unique = true, nullable = false)
	private String username;
	
	@Column(unique = true, nullable = false)
	private String email;
	
	@Column(name = "password_hash", nullable = false)
	private String passwordHash;
	
	@Column(nullable = false)
	private boolean enabled = true;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<RoleEntity> roles;
	
	public UserEntity(UUID id, String username, String email, String passwordHash, boolean enabled, Set<RoleEntity> roles) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.passwordHash = passwordHash;
		this.enabled = enabled;
		this.roles = roles;
	}
	
	public UserEntity(String username, String email, String passwordHash, boolean enabled, Set<RoleEntity> roles) {
		this.username = username;
		this.email = email;
		this.passwordHash = passwordHash;
		this.enabled = enabled;
		this.roles = roles;
	}
	
	public UserEntity() {
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

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
	}
}
