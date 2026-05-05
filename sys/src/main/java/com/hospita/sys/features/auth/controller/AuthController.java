package com.hospita.sys.features.auth.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hospita.sys.features.auth.entity.ApiResponse;
import com.hospita.sys.features.auth.entity.Doctor;
import com.hospita.sys.features.auth.entity.User;
import com.hospita.sys.features.auth.service.AuthService;
import com.hospita.sys.features.auth.service.CloudinaryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import tools.jackson.databind.ObjectMapper;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody User user) {
        return authService.login(user);
    }

    // consumes = {"multipart/form-data"}
    // consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> signup(@Valid @RequestPart("user") User user,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        try {
            if (files == null) {
                files = new ArrayList<>();
            }
        } catch (Exception e) {
            files = new ArrayList<>();
        }
        return authService.signup(user, files);
    }

    @PostMapping(value = "/signuptest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<ApiResponse> signup1(
        @RequestParam("user") String user,
    @RequestParam("files") List<MultipartFile> files
) throws Exception {

    ObjectMapper mapper = new ObjectMapper();
    User user1 = mapper.readValue(user, User.class);

    return authService.signup(user1, files);
}

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletRequest requesttBody) {
        return authService.logout(requesttBody);
    }

    // @PostMapping("/doctors/{id}/certificates")
    // public ResponseEntity<?> uploadCertificates(
    // @PathVariable Long id,
    // @RequestParam("files") List<MultipartFile> files) {

    // return cloudinaryService.uploadCertificates(id, files);
    // }

    @GetMapping("/admin/doctors/{id}")
    public Doctor getDoctor(@PathVariable Long id) {
        return cloudinaryService.getDoctor(id);
    }

}
