package br.com.pradella;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.pradella.halpdesks.entity.User;
import br.com.pradella.halpdesks.enuns.ProfileEnum;
import br.com.pradella.halpdesks.repository.UserRepo;

@SpringBootApplication
public class PradellaHelpDeskApplication {

	public static void main(String[] args) {
		SpringApplication.run(PradellaHelpDeskApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(UserRepo userRepo, PasswordEncoder passwordEncoder) {
		return args -> {
			initUsers(userRepo, passwordEncoder);
		};
	}
	
	private void initUsers(UserRepo userRepo, PasswordEncoder passwordEncoder) {
		User admin = new User();
		admin.setEmail("admin@helpdesk.com");
		admin.setPassword(passwordEncoder.encode("123456"));
		admin.setProfile(ProfileEnum.ROLE_ADMIN);
		
		
		User user = userRepo.findByEmail("userRepo");
		if (user == null) {
			userRepo.save(admin);
		}
	}
	
}
