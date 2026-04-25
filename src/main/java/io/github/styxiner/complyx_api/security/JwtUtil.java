package io.github.styxiner.complyx_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final long expiration;
    private final long refreshExpiration;

    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expiration, @Value("${jwt.refresh_expiration}") long refreshExpiration) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expiration = expiration;
        this.refreshExpiration = refreshExpiration;
    }

    public String generateToken(UserDetails userDetails) {
        return buildToken(userDetails.getUsername(), expiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(userDetails.getUsername(), refreshExpiration);
    }

    private String buildToken(String username, long expirationMs) {
        return Jwts.builder().subject(username).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + expirationMs)).signWith(key).compact();
    }

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);

        return claims != null ? claims.getSubject() : null;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        Claims claims = extractAllClaims(token);

        if (claims == null) return false;

        String username = claims.getSubject();
        Date expirationDate = claims.getExpiration();

        return username != null && username.equals(userDetails.getUsername()) && expirationDate.after(new Date());
    }

    public boolean isTokenExpired(String token) {
        Claims claims = extractAllClaims(token);
        
        return claims == null || claims.getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        try {
    	    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        } catch (Exception e) {
            return null;
        }
    }
}
