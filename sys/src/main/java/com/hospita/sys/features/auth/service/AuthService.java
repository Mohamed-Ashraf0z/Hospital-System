package com.hospita.sys.features.auth.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hospita.sys.features.auth.entity.ApiResponse;
import com.hospita.sys.features.auth.entity.DataResponse;
import com.hospita.sys.features.auth.entity.User;
import com.hospita.sys.features.auth.helper.JwtUtil;
import com.hospita.sys.features.auth.repo.AuthRepo;
import com.hospita.sys.features.patient.entity.Patient;
import com.hospita.sys.features.patient.repo.PatientRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthService {
    @Autowired
    private AuthRepo authRepo;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private PatientRepository patientrepo;

    public ResponseEntity<ApiResponse> login(User user) {
        var dbUser = authRepo.findByEmail(user.getEmail());

        if (dbUser.isPresent() &&
                BCrypt.checkpw(user.getPassword(), dbUser.get().getPassword()) && dbUser.get().getAuth() == true) {

            String token = jwtUtil.generateToken(user, dbUser);
            // tokenBlacklistService.blacklistToken(token, jwtUtil.getRemainingTime(token));

            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("id", dbUser.get().getId());
            data.put("role", dbUser.get().getRole());

            return ResponseEntity.ok(
                    new ApiResponse(
                            true,
                            "login success",
                            data,
                            null));
        }
        return ResponseEntity.ok(
                new ApiResponse(
                        false,
                        "login failed",
                        new DataResponse(
                                user.getAuth(),
                                user.getUsername(),
                                user.getRole(),
                                user.getId()),
                        null));
    }

    public ResponseEntity<ApiResponse> signup(User user, List<MultipartFile> files) {

        user.authfalse();

        if (authRepo.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.ok(
                    new ApiResponse(false, "Email already exist", null, null));
        }

        if (!(user.getRole().contains("Patient")) && !(user.getRole().contains("Doctor"))) {
            return ResponseEntity.ok(
                    new ApiResponse(false, "invalid role", null, null));
        }

        if (user.getRole().contains("Patient")) {
            user.authtrue();
        }

        user.hashpassword();
        user.encryptPhone();

        User savedUser = authRepo.save(user);
        if (user.getRole().contains("Patient")) {
            Patient patient = new Patient();
            patient.setId(user.getId());
            patientrepo.save(patient);
            
            // Assign default Patient permissions
            savedUser.getPermissions().addAll(java.util.Set.of(
                "GET_DOCTORS", 
                "GET_DOCTOR", 
                "GET_HISTORY", 
                "GET_MY_APPOINTMENTS", 
                "MAKE_APPOINTMENT", 
                "CANCEL_APPOINTMENT", 
                "MAKE_HISTORY"
            ));
            authRepo.save(savedUser);
        }
        

        if (user.getRole().contains("Doctor")) {
            if (files.isEmpty()) {
                return ResponseEntity.ok(
                        new ApiResponse(false, "upload certificates", null, null));
            }

            cloudinaryService.uploadCertificates(savedUser.getId(), files);
            
            // Assign default Doctor permissions
            savedUser.getPermissions().addAll(java.util.Set.of(
                "UPDATE_BIO", 
                "ADD_AVAILABILITY", 
                "GET_AVAILABILITY", 
                "GET_APPOINTED_PATIENTS", 
                "SEND_REPORT",
                "GET_HISTORY"
            ));
            authRepo.save(savedUser);
        }

        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "signup success",
                        new DataResponse(
                                savedUser.getAuth(),
                                savedUser.getUsername(),
                                savedUser.getRole(),
                                savedUser.getId()),
                        null));
    }

    public ResponseEntity<ApiResponse> logout(HttpServletRequest request) {

        String token = extractToken(request);
        if (token == null || !jwtUtil.isValidToken(token)) {
            return ResponseEntity.ok(
                    new ApiResponse(
                            false,
                            "logout failed",
                            null,
                            null));
        }

        long expiration = jwtUtil.getRemainingTime(token);

        tokenBlacklistService.blacklistToken(token, expiration);

        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "logout success",
                        null,
                        null));
    }

    private String extractToken(HttpServletRequest request) {
        if (request == null || request.getHeader("Authorization") == null) {
            throw new RuntimeException("JWT is missing !!");
        }
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // يشيل "Bearer "
        }

        return null;
    }

}

// public ResponseEntity<ApiResponse> logout(HttpServletRequest request) {
// String token = extractToken(request); // هقولك عليها تحت
// // blacklistService.add(token);

// return ResponseEntity.ok(
// new ApiResponse(true, "logout success", token, null)
// );
// }

// private String extractToken(HttpServletRequest request) {
// String header = request.getHeader("Authorization");

// if (header != null ) {
// return header;
// }
// return null;
// }

// private final String SECRET = "my-secret-key-my-secret-key-my-secret-key";

// private Key getSignKey() {
// return Keys.hmacShaKeyFor(SECRET.getBytes());
// }

// public String extractUsername(String token) {
// return Jwts.parser()
// .verifyWith(getSignKey())
// .build()
// .parseSignedClaims(token)
// .getPayload()
// .getSubject();
// }

// public String verifytoken(String token){
// String username = extractUsername(token);
// System.out.println(username);
// }

//////////// underwork//////////////////////////////////////////////////