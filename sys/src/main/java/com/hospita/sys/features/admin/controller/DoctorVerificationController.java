package com.hospita.sys.features.admin.controller;

import com.hospita.sys.features.admin.exception.AdminAccessDeniedException;
import com.hospita.sys.features.admin.dto.DoctorPendingDto;
import com.hospita.sys.features.admin.dto.DoctorVerificationDecisionDto;
import com.hospita.sys.features.admin.service.DoctorVerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.List;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@RestController
@RequestMapping("/api/admin/doctors")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.OPTIONS})
public class DoctorVerificationController {

    private final DoctorVerificationService verificationService;
    private final String SECRET = "my-secret-key-my-secret-key-my-secret-key";

    public DoctorVerificationController(DoctorVerificationService verificationService) {
        this.verificationService = verificationService;
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

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingDoctors(HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new AdminAccessDeniedException();
        }
        List<DoctorPendingDto> pendingDoctors = verificationService.getPendingDoctors();
        return ResponseEntity.ok(pendingDoctors);
    }

    @PutMapping("/{id}/verify")
    public ResponseEntity<?> verifyDoctor(@PathVariable Long id, @RequestBody DoctorVerificationDecisionDto decision, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new AdminAccessDeniedException();
        }
        String resultMessage = verificationService.verifyDoctor(id, decision);
        return ResponseEntity.ok(resultMessage);
    }
}
