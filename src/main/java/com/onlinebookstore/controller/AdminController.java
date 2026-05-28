package com.onlinebookstore.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinebookstore.dto.UserRegisterRequest;
import com.onlinebookstore.dto.UserResponseDTO;

import com.onlinebookstore.service.AppUserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	public AppUserService userService;

//	@PutMapping("/update/{id}")
//	public UserResponseDTO updateUser(@PathVariable int id, @RequestBody UserRegisterRequest userRequert,
//			Authentication authentication) throws AccessDeniedException {
//
//		String authUser = authentication.getName();
//
//		return userService.updateUserByIdSecure(id, userRequert, authUser);
//	}

	@PostMapping
	public UserResponseDTO createUser(@RequestBody UserRegisterRequest user) {

		return userService.createUser(user);

	}

	@GetMapping
	public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
		List<UserResponseDTO> users = userService.getAllUser();
		return ResponseEntity.ok(users);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
		UserResponseDTO user = userService.getUserById(id);
		return ResponseEntity.ok(user);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id,
			@RequestBody UserRegisterRequest userRequest) {
		UserResponseDTO updatedUser = userService.userUpdate(id, userRequest);
		return ResponseEntity.ok(updatedUser);
	}

	@DeleteMapping("/{id}")
	public String deleteUser(@PathVariable UUID id) {

		userService.deleteUserById(id);
		return "User Deleted Successfully";
	}

}
