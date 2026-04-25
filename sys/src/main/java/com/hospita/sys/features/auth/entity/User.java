package com.hospita.sys.features.auth.entity;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "Username is required")
    private String username;
    // @NotBlank(message = "Role is required")
    private String role;
    @NotBlank(message = "Password is required")
    private String password;
    @Email
    @NotBlank(message = "Email is required")
    private String email;
    private String phone;
    private boolean auth = false;
    private boolean isFrozen = false;


    public String getRole() {
        return role;
    }


    public void hashpassword() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
    this.password = encoder.encode(this.password);
    }


    // Phone encryption and decryption
    private static final String SECRET_KEY = "1234567890123456"; // 16 chars only REQUIRED for AES-128

    private SecretKeySpec getKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
    }

    public void encryptPhone() {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, getKey());
            this.phone = Base64.getEncoder()
                    .encodeToString(cipher.doFinal(this.phone.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting phone", e);
        }
    }

    public String decryptPhone() {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, getKey());
            return new String(cipher.doFinal(Base64.getDecoder().decode(this.phone)));
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting phone", e);
        }
    }


    public void authtrue() {
        this.auth = true;
    }
    public void authfalse() {
        this.auth = false;
    }

    // public String getall() {
    //     return role + " " + username + " " + password;
    // }

    // public String getpass() {
    //     return password;
    // }

    public boolean getAuth() {
        return auth;
    }

}