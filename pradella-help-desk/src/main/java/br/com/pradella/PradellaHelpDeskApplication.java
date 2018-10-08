package br.com.pradella;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PradellaHelpDeskApplication {

	public static void main(String[] args) {
		SpringApplication.run(PradellaHelpDeskApplication.class, args);
	}
	

/**	
 * @Bean
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
	} **/
	
}
