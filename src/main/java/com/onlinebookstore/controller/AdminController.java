package com.onlinebookstore.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinebookstore.dto.DashBoardActivity;
import com.onlinebookstore.dto.UserRegisterRequestDto;
import com.onlinebookstore.dto.UserResponseDTO;
import com.onlinebookstore.entity.AppUser;
import com.onlinebookstore.entity.Watchlist;
import com.onlinebookstore.repository.BookRepository;
import com.onlinebookstore.repository.RefreshTokenRepository;
import com.onlinebookstore.repository.UserRepository;
import com.onlinebookstore.repository.WatchlistRepository;
import com.onlinebookstore.service.AppUserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	public AppUserService userService;

	@Autowired
	public UserRepository userRepo;

	@Autowired
	public BookRepository bookRepo;
	
	@Autowired
	public WatchlistRepository watchRepo;
	
	@Autowired
	public RefreshTokenRepository token;

	@GetMapping("/counts")
	public Map<String, Object> getDashboardCounts() {

		Map<String, Object> dashboardCounts = new HashMap<>();

		dashboardCounts.put("totalUsers", userRepo.count());

		dashboardCounts.put("totalBooks", bookRepo.count());

		dashboardCounts.put("revenue", "1234");

		dashboardCounts.put("totalSubscriptions", "50");

		dashboardCounts.put("recent-users", userRepo.findTop5ByOrderByCreatedAtDesc());

		return dashboardCounts;

	}

	@GetMapping("/recent-users")
	public List<DashBoardActivity> getRecentUsers() {

	    List<DashBoardActivity> activities =
	            new ArrayList<>();

	    // Recent Users
	    List<AppUser> users =
	            userRepo.findTop5ByOrderByCreatedAtDesc();

	    for (AppUser user : users) {

	        DashBoardActivity activity =
	                new DashBoardActivity();

	        activity.setMessage(
	                user.getUserName() + " registered"
	        );

	        activity.setTime(
	                user.getCreatedAt()
	        );

	        activities.add(activity);
	    }

	    // Recent Watchlist
	    List<Watchlist> watchlists =
	            watchRepo.findTop5ByOrderByCreatedAtDesc();

	    for (Watchlist watchlist : watchlists) {

	        DashBoardActivity activity =
	                new DashBoardActivity();

	        activity.setMessage(
	                watchlist.getBook().getTitle()
	                + " added to watchlist"
	        );

	        activity.setTime(
	                watchlist.getCreatedAt()
	        );

	        activities.add(activity);
	    }

	    return activities;
	}
	@GetMapping
	public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
		List<UserResponseDTO> users = userService.getAllUser();
		return ResponseEntity.ok(users);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id,
			@RequestBody UserRegisterRequestDto userRequest, Authentication authenticate) {
		String loggedInEmail = authenticate.getName();
		UserResponseDTO updatedUser = userService.updateUserByIdSecure(id, userRequest, loggedInEmail);

		return ResponseEntity.ok(updatedUser);
	}

	@DeleteMapping("/delete/{id}")
	public String deleteUser(@PathVariable UUID id) {

		 token.deleteByUser_UserId(id);
		
		userService.deleteUserById(id);
		return "User Deleted Successfully";
	}

}
