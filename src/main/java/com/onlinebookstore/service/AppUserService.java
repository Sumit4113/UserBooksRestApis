package com.onlinebookstore.service;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onlinebookstore.customexception.ResourceNotFoundException;
import com.onlinebookstore.dto.UserRegisterRequestDto;
import com.onlinebookstore.dto.UserResponseDTO;
import com.onlinebookstore.entity.AppUser;
import com.onlinebookstore.repository.UserRepository;

@Service
public class AppUserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public UserResponseDTO registerUser(UserRegisterRequestDto userRegister) {

		if (userRegister.getUserEmail() == null || userRegister.getUserEmail().isBlank()) {

			throw new IllegalArgumentException("Email is Required");
		}

		AppUser user = new AppUser();
		user.setUserName(userRegister.getUserName());
		user.setUserEmail(userRegister.getUserEmail());
		user.setUserRole("USER");
		user.setUserPassword(passwordEncoder.encode(userRegister.getUserPassword()));
        user.setCreatedAt(LocalDateTime.now());
		AppUser saved = userRepo.save(user);


		return mapToResponse(saved);

	}

	public List<UserResponseDTO> getAllUser() {

		List<AppUser> users = userRepo.findAll();

		List<UserResponseDTO> response = new ArrayList<UserResponseDTO>();

		for (AppUser user : users) {
			UserResponseDTO userResponse = mapToResponse(user);
			response.add(userResponse);
		}

		return response;

	}

	public UserResponseDTO getUserById(UUID id) {

		AppUser user = userRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with thire id "));

		return mapToResponse(user);
	}

	public UserResponseDTO updateUserByIdSecure(UUID id, UserRegisterRequestDto userRequest, String loggedInEmail)
			throws AccessDeniedException {

		AppUser user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

		// Check if ADMIN
		if (!isAdmin(loggedInEmail)) {

			// USER can update ONLY their own account
			if (!loggedInEmail.equals(user.getUserEmail())) {
				throw new AccessDeniedException("You cannot update another user");
			}

			// USER is NOT allowed to change role or email
			user.setUserName(userRequest.getUserName());
			user.setUserPassword(passwordEncoder.encode(userRequest.getUserPassword()));

		} else {
			// ADMIN can update everything
			user.setUserName(userRequest.getUserName());
			user.setUserEmail(userRequest.getUserEmail());
			user.setUserPassword(userRequest.getUserPassword());
			user.setUserPassword(passwordEncoder.encode(userRequest.getUserPassword()));
		}

		AppUser savedUser = userRepo.save(user);
		return mapToResponse(savedUser);
	}

	private boolean isAdmin(String email) throws AccessDeniedException {
		AppUser user = userRepo.findByUserEmail(email);

		if (user == null) {
			throw new AccessDeniedException("user not found ");
		}

		return user.getUserRole().equals("ROLE_ADMIN");
	}

	public void deleteUserById(UUID id) {

		AppUser targetUser = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		userRepo.delete(targetUser);
	}

	
	public UserResponseDTO mapToResponse(AppUser user) {

		UserResponseDTO userResponse = new UserResponseDTO();

		userResponse.setUserName(user.getUserName());
		userResponse.setUserEmail(user.getUserEmail());
		userResponse.setUserId(user.getUserId());
		userResponse.setUserRole(user.getUserRole());

		return userResponse;

	}

}
