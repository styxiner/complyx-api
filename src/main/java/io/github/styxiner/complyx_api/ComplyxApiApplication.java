package io.github.styxiner.complyx_api;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.github.styxiner.complyx_api.users.RoleEntity;
import io.github.styxiner.complyx_api.users.RoleRepository;
import io.github.styxiner.complyx_api.users.UserEntity;
import io.github.styxiner.complyx_api.users.UserRepository;

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
        // Crear o recuperar roles
        RoleEntity adminRole = getOrCreateRole("admin");
        RoleEntity tecnicoRole = getOrCreateRole("tecnico");
        RoleEntity auditorRole = getOrCreateRole("auditor");
        RoleEntity tecnicoAuditorRole = getOrCreateRole("tecnico_auditor");

        // Crear usuarios
        createUserIfNotExists("admin", "admin@complyx.local", "admin", Set.of(adminRole));
        createUserIfNotExists("tecnico", "tecnico@complyx.local", "tecnico", Set.of(tecnicoRole));
        createUserIfNotExists("auditor", "auditor@complyx.local", "auditor", Set.of(auditorRole));
        createUserIfNotExists("tecnico_auditor", "tecnico_auditor@complyx.local", "tecnico_auditor", Set.of(tecnicoAuditorRole));
    }
    
    // Método reutilizable para crear roles
    private RoleEntity getOrCreateRole(String roleName) {
        Optional<RoleEntity> existingRole = roleRepository.findByRoleName(roleName);

        if (existingRole.isPresent()) {
            return existingRole.get();
        } else {
            return roleRepository.save(new RoleEntity(null, roleName));
        }
    }
    
    // Lo mismo pero para los usuarios de pruebas
    private void createUserIfNotExists(String username, String email, String password, Set<RoleEntity> roles) {
        if (!userRepository.existsByUsername(username)) {
            String passwordHash = new BCryptPasswordEncoder().encode(password);
            UserEntity user = new UserEntity(username, email, passwordHash, true, roles);
            userRepository.save(user);

            // Construir string de roles
            String rolesStr = "";
            for (RoleEntity role : roles) {
                if (!rolesStr.isEmpty()) {
                    rolesStr += ", ";
                }
                rolesStr += role.getRoleName();
            }

            System.out.println("User " + username + " created with roles: [" + rolesStr + "]");
        } else {
            System.out.println("User " + username + " already exists.");
        }
    }
}
