package io.github.styxiner.complyx_api.security;

import lombok.Data;

@Data
public class RefreshRequestDTO {

    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}