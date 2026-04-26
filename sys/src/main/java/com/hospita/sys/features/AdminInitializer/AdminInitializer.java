package com.hospita.sys.features.AdminInitializer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hospita.sys.features.auth.entity.User;
import com.hospita.sys.features.auth.repo.AuthRepo;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner initAdmin(AuthRepo authRepo) {
        return args -> {

            String adminEmail = "admin@system.com";

            if (authRepo.findByEmail(adminEmail).isEmpty()) {

                User admin = new User();

                admin.setUsername("SuperAdmin");
                admin.setEmail(adminEmail);
                admin.setRole("Admin");
                admin.setPhone("01000000000");

                admin.setPassword("Admin@123");

                admin.authtrue();

                admin.hashpassword();
                admin.encryptPhone();

                authRepo.save(admin);

                System.out.println("Admin created successfully");
            } else {
                System.out.println("Admin already exists");
            }
        };
    }
}