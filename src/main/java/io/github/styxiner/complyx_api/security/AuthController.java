package io.github.styxiner.complyx_api.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService service) {
        this.authService = service;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refresh(@RequestBody RefreshRequestDTO dto) {
        return ResponseEntity.ok(authService.refreshToken(dto.getRefreshToken()));
    }

    @PostMapping("/logout")
    /*public ResponseEntity<Void> logout(Authentication authentication) {
    	
    }*/

    // Como los tokens son stateless, no existe de momento un proceso de cierre de sesión. Como mitigación, la duración de los tokens es reducida.
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String header) {
    	
    	if (header == null || !header.startsWith("Bearer ")) {
    		return ResponseEntity.badRequest().build();
    	}
    	
    	String token = header.replace("Bearer ", "");
        
        authService.logout(token);

        return ResponseEntity.ok().build();
    }
}