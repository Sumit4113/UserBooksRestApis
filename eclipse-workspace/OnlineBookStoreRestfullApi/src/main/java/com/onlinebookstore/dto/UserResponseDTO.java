package com.onlinebookstore.dto;

import java.util.UUID;

public class UserResponseDTO {

	private UUID userId;
	private String userName;
	private String userEmail;
	private String userRole;

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID uuid) {
		this.userId = uuid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

}
