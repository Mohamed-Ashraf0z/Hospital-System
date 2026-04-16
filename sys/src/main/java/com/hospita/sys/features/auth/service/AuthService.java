package com.hospita.sys.features.auth.service;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public String login(User user){
        user.hashpassword();
        if( authRepo.findByUsername(user.getUsername()).isPresent() &&
        authRepo.findByUsername(user.getUsername()).get().getPassword().equals(user.getPassword())){
            // String token = jwtService.generateToken(user.getUsername());
            String token = Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // ساعة
                .signWith(getSignKey())
                .compact();
            return "login success " + token;
        }
        return "login failed " + user.getUsername() + " " + user.getPassword() + " " + authRepo.findByUsername(user.getUsername()).get().getPassword();
    }

    public String signup(User user){
        if (authRepo.findByUsername(user.getUsername()).isPresent()){
            return "existing username";
        }
        if(user.getRole().contains("Doctor")){
            return "you re doctor !!";
        }
        if(!(user.getRole().contains("Patient"))){
            return "wrong role";
        }

        user.hashpassword();
        authRepo.save(user);
        return "signup success";
    }

}
