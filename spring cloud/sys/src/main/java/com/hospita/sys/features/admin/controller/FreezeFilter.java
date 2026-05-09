package com.hospita.sys.features.admin.controller;

import com.hospita.sys.features.auth.entity.User;
import com.hospita.sys.features.auth.repo.AuthRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Optional;

@Component
public class FreezeFilter extends OncePerRequestFilter {

    private final AuthRepo authRepo;
    private final String SECRET = "my-secret-key-my-secret-key-my-secret-key";

    public FreezeFilter(AuthRepo authRepo) {
        this.authRepo = authRepo;
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(getSignKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                
                String email = claims.getSubject();
                if (email != null) {
                    Optional<User> userOpt = authRepo.findByEmail(email);
                    if (userOpt.isPresent() && userOpt.get().isFrozen()) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"success\": false, \"message\": \"Your account is frozen. You cannot perform this action.\"}");
                        return; // Stop the request from proceeding
                    }
                }
            } catch (Exception e) {
                // If token is invalid, just ignore here; the specific controllers will handle it
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
