package io.github.styxiner.complyx_api.users;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    
    public Page<UserDTO> getAllUsers(UserFilter filter, Pageable pageable) {
    	
    	// Busca en la bbdd con los filtros dinámicos definidos
    	Page<UserEntity> page = userRepo.findAll(UserSpecifications.build(filter), pageable );

    	List<UserDTO> list = new java.util.ArrayList<>();

    	for (UserEntity user : page.getContent()) {
    	    list.add(mapper.toDTO(user));
    	}

    	return new PageImpl<>(list, pageable, page.getTotalElements());
    }
    
    public UserDTO getUserById(UUID id) {
    	UserEntity user = userRepo.findById(id).orElseThrow(new Supplier<RuntimeException>() {
    		@Override
    		public RuntimeException get() {
    			return new RuntimeException("User does not exist");
    		}
    	});
    	
    	return mapper.toDTO(user);
    }

    public UserDTO create(UserCreateDTO dto) {

        if (userRepo.existsByUsername(dto.getUsername())) {
        	throw new RuntimeException("Username exists");
        }
            

        if (userRepo.existsByEmail(dto.getEmail())) {
        	throw new RuntimeException("Email exists");
        }
            
        UserEntity user = mapper.toEntity(dto);
        user.setPasswordHash(encoder.encode(dto.getPassword()));

        if (dto.getRoleIds() != null) {
            Set<RoleEntity> roles = new HashSet<>(roleRepo.findAllById(dto.getRoleIds()));
            user.setRoles(roles);
        }

        return mapper.toDTO(userRepo.save(user));
    }
    
    public UserDTO update(UserUpdateDTO dto) {
    	UserEntity user = userRepo.findById(dto.getId()).orElseThrow(new Supplier<RuntimeException>() {
    		@Override
    		public RuntimeException get() {
    			return new RuntimeException("user does not exist");
    		}
    	});
    	
    	if (dto.getUsername() != null) {
    		user.setUsername(dto.getUsername());
    	}
    	
    	if (dto.getEmail() != null) {
    		user.setEmail(dto.getEmail());
    	}
    	
    	if (dto.getPassword() != null) {
    		user.setPasswordHash(encoder.encode(dto.getPassword()));
    	}
    	
    	return mapper.toDTO(userRepo.save(user));
    }
    
    public void delete(UUID id) {
    	if (!userRepo.existsById(id)) {
    		throw new RuntimeException("User does not exist");
    	}
    	
    	userRepo.deleteById(id);
    }

    public void assignRole(UUID userId, UUID roleId) {

    	// Ahora que caigo, podría marcarme un nuevo ExceptionFactory para no tener que estar escribiendo tantas
        UserEntity user = userRepo.findById(userId).orElseThrow();

        RoleEntity role = roleRepo.findById(roleId).orElseThrow();

        user.getRoles().add(role);
        userRepo.save(user);
    }
    
    public void unassignRole(UUID userId, UUID roleId) {
    	UserEntity user = userRepo.findById(userId).orElseThrow(new Supplier<RuntimeException>() {
    		@Override
    		public RuntimeException get() {
    			return new RuntimeException("user does not exist");
    		}
    	});
    	
    	RoleEntity role = roleRepo.findById(roleId).orElseThrow(new Supplier<RuntimeException>() {
    		@Override
    		public RuntimeException get() {
    			return new RuntimeException("role does not exist");
    		}
    	});
    	
    	user.getRoles().remove(role);
    	userRepo.save(user);
    }
}