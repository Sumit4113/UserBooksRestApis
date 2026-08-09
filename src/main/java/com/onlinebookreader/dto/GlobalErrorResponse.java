package com.onlinebookreader.dto;

import org.springframework.http.HttpStatus;

public record GlobalErrorResponse(String message, HttpStatus status ,int statusCode) {

	

}
