package com.hospita.sys.features.auth.helper;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hospita.sys.features.auth.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {


    private final String SECRET = "my-secret-key-my-secret-key-my-secret-key";

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(User user, Optional<User> dbUser) {
        return Jwts.builder()
        .setSubject(user.getEmail())
        .claim("role", dbUser.get().getRole())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
        .signWith(getSignKey())
        .compact();
    }

    public boolean isValidToken(String token) {
    try {
        Jwts.parser()
                .setSigningKey(getSignKey())
                .parseClaimsJws(token);
        return true;
    } catch (Exception e) {
        System.out.println("Invalid token: " + e.getMessage());
        return false;
    }
}

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }


private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(getSignKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
}
    public long getRemainingTime(String token) {
        Date expiration = extractExpiration(token);
        long now = System.currentTimeMillis();

        return expiration.getTime() - now;
    }
}