package com.onlinebookstore.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinebookstore.dto.UserRegisterRequest;
import com.onlinebookstore.dto.UserResponseDTO;

import com.onlinebookstore.service.AppUserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

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
	public List<UserResponseDTO> getAllUsers() {

		return userService.getAllUser();
	}

	@GetMapping("/{id}")
	public UserResponseDTO getById(@PathVariable UUID id) {

		return userService.getUserById(id);
	}

	@DeleteMapping("/{id}")
	public String deleteUser(@PathVariable UUID id) {

		userService.deleteUserById(id);
		return "User Deleted Successfully";
	}

}
