package io.github.styxiner.complyx_api.users;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
	RoleDTO toDTO(RoleEntity role);
	RoleEntity toEntity(RoleCreateDTO role);
/*	
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
*/
}
