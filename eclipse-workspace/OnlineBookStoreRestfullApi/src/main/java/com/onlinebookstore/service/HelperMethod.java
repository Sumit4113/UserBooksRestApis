package com.onlinebookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.onlinebookstore.entity.AppUser;
import com.onlinebookstore.repository.UserRepository;

@Component
public class HelperMethod implements CommandLineRunner {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		
		if(!userRepo.existsByUserEmail("admin01@gmail.com")) {
			
			AppUser user = new AppUser();
			
			
			user.setUserName("Admin");
			user.setUserEmail("admin01@gmail.com");
			user.setUserPassword(passwordEncoder.encode("admin123"));
			user.setUserRole("ADMIN");
			
			userRepo.save(user);
			
			System.out.println("Default Admin id : admin01@gmail.com" );
		}
		
		
		
	}

}
