package Capston.camo;

import Capston.camo.domain.Account;
import Capston.camo.domain.Role;
import Capston.camo.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class CamoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamoApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService){
		return args -> {
			userService.saveRole(new Role(null,"ROLE_USER"));
			userService.saveRole(new Role(null,"ROLE_MANAGER"));
			userService.saveRole(new Role(null,"ROLE_ADMIN"));
			userService.saveRole(new Role(null,"ROLE_SUPER_ADMIN"));

			userService.saveAccount(new Account(null,"first","1234","email1@naver.com",new ArrayList<>()));
			userService.saveAccount(new Account(null,"second","1234","email2@naver.com",new ArrayList<>()));
			userService.saveAccount(new Account(null,"third","1234","email3@naver.com",new ArrayList<>()));
			userService.saveAccount(new Account(null,"fourth","1234","email4@naver.com",new ArrayList<>()));

			userService.addRoleToAccount("email1@naver.com","ROLE_USER");
			userService.addRoleToAccount("email2@naver.com","ROLE_MANAGER");
			userService.addRoleToAccount("email3@naver.com","ROLE_ADMIN");
			userService.addRoleToAccount("email4@naver.com","ROLE_SUPER_ADMIN");
		};
	}

}
