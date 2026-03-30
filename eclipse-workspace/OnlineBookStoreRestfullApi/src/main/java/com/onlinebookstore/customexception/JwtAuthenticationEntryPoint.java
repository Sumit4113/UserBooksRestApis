package com.onlinebookstore.customexception;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");

		String body = """
				{
				  "status": 401,
				  "error": "Unauthorized",
				  "message": "Invalid or missing JWT token",
				  "path": "%s"
				}
				""".formatted(request.getRequestURI());

		response.getWriter().write(body);
	}

}
