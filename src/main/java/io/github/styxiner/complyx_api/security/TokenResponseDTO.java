package io.github.styxiner.complyx_api.security;

public class TokenResponseDTO {

    private String accessToken;
    private String refreshToken;
    private long expiresIn;

    public TokenResponseDTO(String a, String r, long e) {
        this.accessToken = a;
        this.refreshToken = r;
        this.expiresIn = e;
    }

    public String getAccessToken() { 
    	return accessToken; 
    }
    public String getRefreshToken() {
    	return refreshToken; 
    }
    
    public long getExpiresIn() {
    	return expiresIn; 
    }
}