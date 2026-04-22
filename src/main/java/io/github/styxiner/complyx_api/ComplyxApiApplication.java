package io.github.styxiner.complyx_api;

import io.github.styxiner.complyx_api.users.RoleEntity;
import io.github.styxiner.complyx_api.users.UserEntity;
import io.github.styxiner.complyx_api.users.UserRepository;
import io.github.styxiner.complyx_api.users.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.context.annotation.Bean;

import java.util.Set;
import java.util.HashSet;
import java.util.Optional;

@SpringBootApplication
public class ComplyxApiApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ComplyxApiApplication.class, args);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) {
	    // Rol administrador
	    RoleEntity adminRole;
	    Optional<RoleEntity> existingRole = roleRepository.findByRoleName("ADMIN");
	    if (existingRole.isPresent()) {
	        adminRole = existingRole.get();
	    } else {
	        adminRole = roleRepository.save(new RoleEntity(null, "ADMIN"));
	    }
	
	    // Usuario administrador
	    if (!userRepository.existsByUsername("admin")) {
	        String passwordHash = new BCryptPasswordEncoder().encode("contraseña");
	        Set<RoleEntity> roles = new HashSet<>();
	        roles.add(adminRole);
	        UserEntity testUser = new UserEntity(null, "admin", "admin@complyx.local", passwordHash, true, roles);
	        userRepository.save(testUser);
	        System.out.println("Test user created!");
	    } else {
	        System.out.println("Test user already exists.");
	    }
	}
}
