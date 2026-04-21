package io.github.styxiner.complyx_api.security;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class TokenResponseDTO {

    private String accessToken;
    private String refreshToken;
    private long expiresIn;
}