package io.github.styxiner.complyx_api.security;

public class RefreshRequestDTO {

    private String refreshToken;

	public RefreshRequestDTO(String refreshToken) {
		super();
		this.refreshToken = refreshToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

    

}