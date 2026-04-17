package com.onlinebookstore.jwt;

import java.io.IOException;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.onlinebookstore.entity.AppUser;
import com.onlinebookstore.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization: " + authHeader);

        // ✅ If no token → just continue
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);

            if (jwtService.isAccessToken(token)) {

                UUID userId = jwtService.getUserId(token);

                AppUser user = userRepository
                        .findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                List<GrantedAuthority> authorities =
                        List.of(() -> user.getUserRole());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user.getUserId().toString(),
                                null,
                                authorities
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
            }

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
        
       
    }
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    	
    	System.out.println("PATH: " + request.getRequestURI());
    	
    	return request.getRequestURI().startsWith("/auth");
    	
    }
    
  
}
