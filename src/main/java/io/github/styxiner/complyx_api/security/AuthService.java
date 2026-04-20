package io.github.styxiner.complyx_api.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl uds;

    public AuthService(AuthenticationManager am, JwtUtil jwtUtil, UserDetailsServiceImpl uds) {
        this.authManager = am;
        this.jwtUtil = jwtUtil;
        this.uds = uds;
    }

    public TokenResponseDTO login(LoginDTO dto) {

        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        UserDetails user = uds.loadUserByUsername(dto.getUsername());

        String access = jwtUtil.generateToken(user);
        String refresh = jwtUtil.generateRefreshToken(user);

        return new TokenResponseDTO(access, refresh, 3600);
    }

    public TokenResponseDTO refreshToken(String refreshToken) {

        String username = jwtUtil.extractUsername(refreshToken);
        UserDetails user = uds.loadUserByUsername(username);

        if (!jwtUtil.validateToken(refreshToken, user)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccess = jwtUtil.generateToken(user);

        return new TokenResponseDTO(newAccess, refreshToken, 3600);
    }

    public void logout(String token) {
        // Falta de implementar
    }
}