package com.hospita.sys.features.auth.entity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


@Entity
@Getter
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


    public String getRole() {
        return role;
    }


    public void hashpassword() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
    this.password = encoder.encode(this.password);
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