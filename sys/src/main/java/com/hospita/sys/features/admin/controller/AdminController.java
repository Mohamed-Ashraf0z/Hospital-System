package com.hospita.sys.features.admin.controller;

import java.security.Key;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hospita.sys.features.admin.exception.AdminAccessDeniedException;
import com.hospita.sys.features.admin.dto.UserDto;
import com.hospita.sys.features.admin.service.AdminService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/admin/users")
public class AdminController {

    private final AdminService adminService;
    private final String SECRET = "my-secret-key-my-secret-key-my-secret-key";

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
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

    @GetMapping
    public ResponseEntity<?> getAllUsers(HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new AdminAccessDeniedException();
        }
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new AdminAccessDeniedException();
        }
        adminService.deleteUser(id);
        return ResponseEntity.ok("User with id " + id + " has been deleted.");
    }

    @PutMapping("/{id}/freeze")
    public ResponseEntity<?> freezeUser(@PathVariable Long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new AdminAccessDeniedException();
        }
        UserDto frozenUser = adminService.freezeUser(id);
        return ResponseEntity.ok(frozenUser);
    }
}
