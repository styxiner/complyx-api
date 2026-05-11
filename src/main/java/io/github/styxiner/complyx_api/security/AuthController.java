package io.github.styxiner.complyx_api.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Autenticación y gestión de tokens JWT")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService service) {
        this.authService = service;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión y obtener tokens JWT")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login correcto, tokens generados"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refrescar el token de acceso mediante un refresh token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Nuevo token de acceso generado"),
        @ApiResponse(responseCode = "401", description = "Refresh token inválido o expirado")
    })
    public ResponseEntity<TokenResponseDTO> refresh(@Valid @RequestBody RefreshRequestDTO dto) {
        return ResponseEntity.ok(authService.refreshToken(dto.getRefreshToken()));
    }

    // Los tokens son stateless: no hay invalidación servidor. La duración reducida actúa como mitigación.
    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesión (stateless — el token expira por TTL)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Logout registrado"),
        @ApiResponse(responseCode = "400", description = "Cabecera Authorization ausente o malformada")
    })
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }
        String token = header.replace("Bearer ", "");
        authService.logout(token);
        return ResponseEntity.ok().build();
    }
}