package io.github.styxiner.complyx_api.users;

import java.util.Optional;

public interface UserRepository {
	Optional<UserEntity> findByUsername(String username);
	Optional<UserEntity> findByEmail(String email);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
}
