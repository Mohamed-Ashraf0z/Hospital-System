package com.hospita.sys.features.admin.controller;

import java.security.Key;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospita.sys.features.admin.exception.AdminAccessDeniedException;
import com.hospita.sys.features.admin.service.PermissionService;
import com.hospita.sys.features.auth.entity.ApiResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/admin/permissions")
public class PermissionController {

    private final PermissionService permissionService;
    private final String SECRET = "my-secret-key-my-secret-key-my-secret-key";

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    private boolean isAdmin(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        String token = authHeader.substring(7);
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String role = claims.get("role", String.class);
            return role != null && role.equalsIgnoreCase("admin");
        } catch (Exception e) {
            return false;
        }
    }

    @PostMapping("/users/{userId}/grant")
    public ResponseEntity<ApiResponse> grantPermission(@PathVariable Long userId, @RequestBody Map<String, String> requestBody, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new AdminAccessDeniedException();
        }
        String permission = requestBody.get("permission");
        if (permission == null || permission.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Permission is required", null, null));
        }

        boolean success = permissionService.grantPermission(userId, permission);
        if (success) {
            return ResponseEntity.ok(new ApiResponse(true, "Permission granted successfully", null, null));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User not found", null, null));
        }
    }

    @PostMapping("/users/{userId}/revoke")
    public ResponseEntity<ApiResponse> revokePermission(@PathVariable Long userId, @RequestBody Map<String, String> requestBody, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new AdminAccessDeniedException();
        }
        String permission = requestBody.get("permission");
        if (permission == null || permission.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Permission is required", null, null));
        }

        boolean success = permissionService.revokePermission(userId, permission);
        if (success) {
            return ResponseEntity.ok(new ApiResponse(true, "Permission revoked successfully", null, null));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User not found or permission not present", null, null));
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse> getUserPermissions(@PathVariable Long userId, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new AdminAccessDeniedException();
        }
        Set<String> permissions = permissionService.getUserPermissions(userId);
        if (permissions != null) {
            return ResponseEntity.ok(new ApiResponse(true, "Permissions retrieved", permissions, null));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User not found", null, null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllPermissions(HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new AdminAccessDeniedException();
        }
        return ResponseEntity.ok(new ApiResponse(true, "All permissions retrieved", permissionService.getAllSystemPermissions(), null));
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<ApiResponse> updatePermissions(@PathVariable Long userId, @RequestBody Set<String> permissions, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new AdminAccessDeniedException();
        }
        boolean success = permissionService.updatePermissions(userId, permissions);
        if (success) {
            return ResponseEntity.ok(new ApiResponse(true, "Permissions updated successfully", null, null));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User not found", null, null));
        }
    }
}
