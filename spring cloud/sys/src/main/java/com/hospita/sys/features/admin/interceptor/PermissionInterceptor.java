package com.hospita.sys.features.admin.interceptor;

import java.security.Key;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.hospita.sys.features.admin.annotation.RequiresPermission;
import com.hospita.sys.features.auth.entity.User;
import com.hospita.sys.features.auth.repo.AuthRepo;
import com.hospita.sys.features.auth.service.TokenBlacklistService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthRepo authRepo;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    private final String SECRET = "my-secret-key-my-secret-key-my-secret-key";

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequiresPermission requiresPermission = handlerMethod.getMethodAnnotation(RequiresPermission.class);

        if (requiresPermission == null) {
            requiresPermission = handlerMethod.getBeanType().getAnnotation(RequiresPermission.class);
        }

        if (requiresPermission != null) {
            String requiredPermission = requiresPermission.value();
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                sendErrorResponse(response, "Authorization token is missing or invalid", HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }

            String token = authHeader.substring(7);

            if (tokenBlacklistService.isBlacklisted(token)) {
                sendErrorResponse(response, "Token is invalid or logged out", HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }

            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(getSignKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String email = claims.getSubject();
                if (email != null) {
                    Optional<User> userOpt = authRepo.findByEmail(email);
                    if (userOpt.isPresent()) {
                        User user = userOpt.get();
                        
                        if (user.isFrozen()) {
                            sendErrorResponse(response, "Your account is frozen. You cannot perform this action.", HttpServletResponse.SC_FORBIDDEN);
                            return false;
                        }

                        String role = user.getRole();
                        if (role != null && role.equalsIgnoreCase("admin")) {
                            return true;
                        }

                        if (request.getRequestURI().contains("/api/admin") && !role.equalsIgnoreCase("admin")) {
                            sendErrorResponse(response, "Access denied: Only admins can access these functions.", HttpServletResponse.SC_FORBIDDEN);
                            return false;
                        }
                        if (request.getRequestURI().contains("/api/doctors") && !role.equalsIgnoreCase("doctor")) {
                            sendErrorResponse(response, "Access denied: Only doctors can access these functions.", HttpServletResponse.SC_FORBIDDEN);
                            return false;
                        }
                        if (request.getRequestURI().contains("/api/patients") && !role.equalsIgnoreCase("patient")) {
                            sendErrorResponse(response, "Access denied: Only patients can access these functions.", HttpServletResponse.SC_FORBIDDEN);
                            return false;
                        }
                        
                        if (!user.getPermissions().contains(requiredPermission)) {
                            sendErrorResponse(response, "You have no permission to use this function", HttpServletResponse.SC_FORBIDDEN);
                            return false;
                        }
                        return true;
                    }
                }
                sendErrorResponse(response, "User not found", HttpServletResponse.SC_FORBIDDEN);
                return false;
            } catch (Exception e) {
                sendErrorResponse(response, "Invalid token", HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }
        return true;
    }

    private void sendErrorResponse(HttpServletResponse response, String message, int status) throws Exception {
        response.setStatus(status);
        response.setContentType("application/json");
        String jsonResponse = String.format(
            "{\"success\": false, \"message\": \"%s\", \"data\": null, \"errors\": null}",
            message
        );
        response.getWriter().write(jsonResponse);
    }
}
