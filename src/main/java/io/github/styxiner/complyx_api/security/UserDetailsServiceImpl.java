package io.github.styxiner.complyx_api.security;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.github.styxiner.complyx_api.users.RoleEntity;
import io.github.styxiner.complyx_api.users.UserEntity;
import io.github.styxiner.complyx_api.users.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(new Supplier<RuntimeException>() {
        	
			@Override
			public RuntimeException get() {
				return new RuntimeException("user does not exist");
			}
        	
        });

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (RoleEntity role : user.getRoles()) {
        	// La convención de SpringSecurity por defecto necesita lo de "ROLE_"
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        }

        boolean enabled = user.isEnabled();
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        return new User(user.getUsername(), user.getPasswordHash(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
