package io.github.styxiner.complyx_api.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }
        UserDetails user = uds.loadUserByUsername(dto.getUsername());
        String access = jwtUtil.generateToken(user);
        String refresh = jwtUtil.generateRefreshToken(user);
        return new TokenResponseDTO(access, refresh, 3600);
    }

    public TokenResponseDTO refreshToken(String refreshToken) {
        String username = jwtUtil.extractUsername(refreshToken);
        UserDetails user = uds.loadUserByUsername(username);
        if (!jwtUtil.validateToken(refreshToken, user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido");
        }
        String newAccess = jwtUtil.generateToken(user);
        return new TokenResponseDTO(newAccess, refreshToken, 3600);
    }

    public void logout(String token) {
        // pendiente de implementar
    }
}