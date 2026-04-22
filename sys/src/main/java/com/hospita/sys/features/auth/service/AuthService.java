package com.hospita.sys.features.auth.service;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.hospita.sys.features.auth.entity.ApiResponse;
import com.hospita.sys.features.auth.entity.User;
import com.hospita.sys.features.auth.repo.AuthRepo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {
    @Autowired
    private AuthRepo authRepo;

    private final String SECRET = "my-secret-key-my-secret-key-my-secret-key";

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

//     public String extractUsername(String token) {
//     return Jwts.parser()
//             .verifyWith(getSignKey())
//             .build()
//             .parseSignedClaims(token)
//             .getPayload()
//             .getSubject();
// }

//     public String verifytoken(String token){
//         String username = extractUsername(token);
//         System.out.println(username);
//     }

////////////underwork//////////////////////////////////////////////////
    public ResponseEntity<ApiResponse> login(User user){
        var dbUser = authRepo.findByEmail(user.getEmail());

if (dbUser.isPresent() &&
    BCrypt.checkpw(user.getPassword(), dbUser.get().getPassword()) && dbUser.get().getAuth() == true) {

    String token = Jwts.builder()
        .subject(user.getEmail())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
        .signWith(getSignKey())
        .compact();

    return ResponseEntity.ok(
        new ApiResponse(
            true,
            "login success",
            token,
            null
        )
    );
}
        return ResponseEntity.ok(
            new ApiResponse(
                false,
                "login failed",
                null,
                null
            )
        );
    }

    public ResponseEntity<ApiResponse> signup(User user){
        user.authfalse();
        if (authRepo.findByEmail(user.getEmail()).isPresent()){
            return ResponseEntity.ok(
                new ApiResponse(
                    false,
                    "Email already exist",
                    null,
                    null
                )
            );
        }
        if(user.getRole().contains("Patient")){
            user.authtrue();
        }
        if(!(user.getRole().contains("Patient")) && !(user.getRole().contains("Doctor"))){
            return ResponseEntity.ok(
                new ApiResponse(
                    false,
                    "invalid role",
                    null,
                    null
                )
            );
        }

        user.hashpassword();
        authRepo.save(user);
        return ResponseEntity.ok(
            new ApiResponse(
                true,
                "signup success",
                null,
                null
            )
        );
    }

    // public ResponseEntity<ApiResponse> logout(User user){

    //     return ResponseEntity.ok(
    //         new ApiResponse(
    //             true,
    //             "logout success",
    //             null,
    //             null
    //         )
    //     );
    // }

//     public ResponseEntity<ApiResponse> logout(HttpServletRequest request) {
//     String token = extractToken(request); // هقولك عليها تحت
//     // blacklistService.add(token);

//     return ResponseEntity.ok(
//         new ApiResponse(true, "logout success", token, null)
//     );
// }

// private String extractToken(HttpServletRequest request) {
//     String header = request.getHeader("Authorization");

//     if (header != null ) {
//         return header;
//     }
//     return null;
// }

}
