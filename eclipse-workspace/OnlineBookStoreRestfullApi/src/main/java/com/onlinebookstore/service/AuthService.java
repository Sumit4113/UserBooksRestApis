package com.onlinebookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onlinebookstore.dto.LoginRequest;
import com.onlinebookstore.dto.UserRegisterRequest;
import com.onlinebookstore.dto.UserResponseDTO;
import com.onlinebookstore.entity.AppUser;
import com.onlinebookstore.jwt.JwtService;
import com.onlinebookstore.repository.UserRepository;

@Service
public class AuthService {

	@Autowired
	public AppUserService userService;
	

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

	public UserResponseDTO register(UserRegisterRequest userResponse) {

		
		return userService.createUser(userResponse);
	}
	
	

   
}
