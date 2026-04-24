package io.github.styxiner.complyx_api.users;

import java.util.HashSet;
import java.util.Set;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
	UserDTO toDTO(UserEntity user);
	UserEntity toEntity(UserCreateDTO userCreateDto);
	
	// Entity a DTO
	default Set<String> mapRoles(Set<RoleEntity> roles) {
		if (roles == null) {
			return null;
		}
		
		Set<String> result = new HashSet<String>();
		
		for (RoleEntity role: roles) {
			result.add(role.getRoleName());
		}
		
		return result;
	}
	
	// DTO a Entity
	default Set<RoleEntity> mapRoleNames(Set<String> roles) {
		if (roles == null) {
			return null;
		}
		
		Set<RoleEntity> result = new HashSet<RoleEntity>();
		
		for (String roleName: roles) {
			RoleEntity role = new RoleEntity();
			role.setRoleName(roleName);
			result.add(role);
		}
		
		return result;
	}
}
