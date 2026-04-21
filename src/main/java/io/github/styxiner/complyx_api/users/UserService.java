package io.github.styxiner.complyx_api.users;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepo, RoleRepository roleRepo, UserMapper mapper, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    public UserDTO create(UserCreateDTO dto) {

        if (userRepo.existsByUsername(dto.getUsername()))
            throw new RuntimeException("Username exists");

        if (userRepo.existsByEmail(dto.getEmail()))
            throw new RuntimeException("Email exists");

        UserEntity user = mapper.toEntity(dto);
        user.setPasswordHash(encoder.encode(dto.getPassword()));

        if (dto.getRoleIds() != null) {
            Set<RoleEntity> roles = new HashSet<>(roleRepo.findAllById(dto.getRoleIds()));
            user.setRoles(roles);
        }

        return mapper.toDTO(userRepo.save(user));
    }

    public void assignRole(UUID userId, UUID roleId) {

        UserEntity user = userRepo.findById(userId)
                .orElseThrow();

        RoleEntity role = roleRepo.findById(roleId)
                .orElseThrow();

        user.getRoles().add(role);
        userRepo.save(user);
    }
}