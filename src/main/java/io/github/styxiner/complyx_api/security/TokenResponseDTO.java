package io.github.styxiner.complyx_api.security;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDTO {

    private String accessToken;
    private String refreshToken;
    private long expiresIn;
}
