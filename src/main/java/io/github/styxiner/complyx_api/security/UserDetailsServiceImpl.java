package io.github.styxiner.complyx_api.security;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) {

        // reemplazar por BD real
        return User.withUsername(username)
                .password("$2a$10$Dow1Z8z3examplehashedpassword") 
                .authorities(Collections.emptyList())
                .build();
    }
}