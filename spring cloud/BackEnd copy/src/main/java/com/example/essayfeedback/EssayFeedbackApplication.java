package com.example.essayfeedback;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.openfeign.EnableFeignClients;
// import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;

@SpringBootApplication
@EnableFeignClients
public class EssayFeedbackApplication {
    public static void main(String[] args) {
        SpringApplication.run(EssayFeedbackApplication.class, args);
    }
	// @Bean
	// public static CommandLineRunner seedAdmin(AuthRepo authRepo) {
	// 	return args -> {
	// 		if (authRepo.findByEmail("master_admin@hospital.com").isEmpty()) {
	// 			User admin = new User();
	// 			admin.setUsername("master_admin");
	// 			admin.setEmail("master_admin@hospital.com");
	// 			admin.setPassword(BCrypt.hashpw("admin123", BCrypt.gensalt()));
	// 			admin.setRole("Admin");
	// 			admin.authtrue();
	// 			authRepo.save(admin);
	// 			System.out.println("Admin user seeded: master_admin@hospital.com / admin123");
	// 		}
	// 	};
	// }

}