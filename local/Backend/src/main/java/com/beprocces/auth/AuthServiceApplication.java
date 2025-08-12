package com.beprocces.auth;

import com.beprocces.auth.model.Role;
import com.beprocces.auth.model.User;
import com.beprocces.auth.repository.RoleRepository;
import com.beprocces.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableScheduling
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}


	@Bean
	public CommandLineRunner runner(
			RoleRepository roleRepository ,
			UserRepository userRepository ,
			PasswordEncoder passwordEncoder

	) {
		return args -> {
			if (roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(
						Role.builder().
								name("USER")
								.nbrUtilisateur(0)
								.description("Rôle par défaut")
								.permissions(Arrays.asList("Création", "Suppression", "Consultation"))
								.status("Actif")
								.build());

			}
			if(!userRepository.findByEmail("california@gym.com").isPresent()){
				Role adminRole = roleRepository.findByName("USER")
						.orElseThrow(()-> new RuntimeException(("ROLE ADMIN not Found")));
				userRepository.save(
						User.builder()
								.email("california@gym.com")
								.firstname("CALIFORNIA ")
								.lastname("CALIFORNIA ")
								.password(passwordEncoder.encode("12345678"))
								.roles(List.of(adminRole))
								.accountLocked(false)
								.enabled(true)
								.createdDate(LocalDate.now())
								.build()
				);
			}
		};
	}
}
