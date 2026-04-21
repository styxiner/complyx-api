package io.github.styxiner.complyx_api.users;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity {
	
	@Id
	@GeneratedValue
	private UUID id;
	
	@Column(name = "rolename", unique = true, nullable = false)
	private String roleName;
}
