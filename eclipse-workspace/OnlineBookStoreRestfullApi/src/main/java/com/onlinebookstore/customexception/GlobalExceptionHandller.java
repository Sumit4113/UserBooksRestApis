package com.onlinebookstore.customexception;

import javax.security.auth.login.CredentialExpiredException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.onlinebookstore.dto.ApiGlobalErrorRecord;
import com.onlinebookstore.dto.GlobalErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandller {

	@ExceptionHandler({
		UsernameNotFoundException.class,
		BadCredentialsException.class,
		CredentialExpiredException.class
	})
	public ResponseEntity<ApiGlobalErrorRecord> customErrorHandler(Exception e, HttpServletRequest request) {

	var apiError = ApiGlobalErrorRecord.of(HttpStatus.BAD_REQUEST.value(), "Bad Request", e.getMessage(), request.getRequestURI(), null);
		
	   return ResponseEntity.badRequest().body(apiError);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<GlobalErrorResponse> handleResourceNotFoundException(
			ResourceNotFoundException resourceException) {

		GlobalErrorResponse internalServerError = new GlobalErrorResponse(resourceException.getMessage(),
				HttpStatus.NOT_FOUND, 404);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(internalServerError);

	}
}
