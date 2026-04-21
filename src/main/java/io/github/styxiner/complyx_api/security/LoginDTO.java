package io.github.styxiner.complyx_api.security;

import lombok.Data;

@Data
public class LoginDTO {

    private String username;
    private String password;
}