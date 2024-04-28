package com.example.AutoSched;

import com.example.AutoSched.Calendar.Calendar;
import com.example.AutoSched.Calendar.CalendarRepository;
import com.example.AutoSched.HttpBodies.AccountCredentials;
import com.example.AutoSched.User.User;
import com.example.AutoSched.User.UserRepository;
import org.mockito.Mockito.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.security.NoSuchAlgorithmException;


@SpringBootApplication
public class AutoSchedApplication {


	public static void main(String[] args) throws NoSuchAlgorithmException {
		SpringApplication.run(AutoSchedApplication.class, args);
	}

	@Bean
	CommandLineRunner initUser(UserRepository userRepository, CalendarRepository calendarRepository) {
		return args -> {
			AccountCredentials liam_credentials = new AccountCredentials("liam_admin", "liam_password");
			User liam_admin = new User(liam_credentials.getName(), liam_credentials.passwordHash());
			Calendar calendar = new Calendar();
			calendarRepository.save(calendar);
			liam_admin.setPrivilege(User.Privilege.ADMIN);
			userRepository.save(liam_admin);
			AccountCredentials Vince = new AccountCredentials("VinceQuatt", "vince_password");
			User VinceQuatt = new User(Vince.getName(), Vince.passwordHash());
			VinceQuatt.setPrivilege(User.Privilege.ADMIN);
			userRepository.save(VinceQuatt);
			AccountCredentials Test = new AccountCredentials("TestUser", "Test");
			User TestUser = new User(Test.getName(), Test.passwordHash());
			TestUser.setPrivilege(User.Privilege.USER);
			userRepository.save(TestUser);
			AccountCredentials Mitra = new AccountCredentials("SMitra", "Test123");
			User SMitra = new User(Mitra.getName(), Mitra.passwordHash());
			SMitra.setPrivilege(User.Privilege.INSTRUCTOR);
			userRepository.save(SMitra);
		};
	}




}
