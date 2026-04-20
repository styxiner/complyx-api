package io.github.styxiner.complyx_api.users;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface RoleRepository {
	Optional<RoleEntity> findByRolename(String name);
	List<RoleEntity> findByIdIn(Set<UUID> roleIds);
}
