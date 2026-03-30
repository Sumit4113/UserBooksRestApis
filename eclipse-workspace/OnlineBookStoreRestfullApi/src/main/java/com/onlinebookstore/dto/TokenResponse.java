package com.onlinebookstore.dto;

import com.onlinebookstore.entity.AppUser;

public record TokenResponse(
		String accessToken, 
		String freshToken, 
		long expiresIn, 
		String tokenType,
		UserResponseDTO userResponse
		)
{
	public static TokenResponse of(String accessToken, String freshToken, long expiresIn, String tokenType, UserResponseDTO userResponse) {
		
		return new TokenResponse(accessToken, freshToken, expiresIn, "Bearer", userResponse );
		
	}
}
