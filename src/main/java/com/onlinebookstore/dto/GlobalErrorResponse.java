package com.onlinebookstore.dto;

import org.springframework.http.HttpStatus;

public record GlobalErrorResponse(String message, HttpStatus status ,int statusCode) {

	

}
