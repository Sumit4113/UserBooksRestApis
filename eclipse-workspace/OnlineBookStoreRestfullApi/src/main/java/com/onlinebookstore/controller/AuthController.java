package com.onlinebookstore.controller;

import java.time.Instant;


import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinebookstore.dto.LoginRequest;
import com.onlinebookstore.dto.RefreshTokenRequest;
import com.onlinebookstore.dto.TokenResponse;
import com.onlinebookstore.dto.UserRegisterRequest;
import com.onlinebookstore.dto.UserResponseDTO;
import com.onlinebookstore.entity.AppUser;
import com.onlinebookstore.entity.RefreshToken;
import com.onlinebookstore.jwt.JwtService;
import com.onlinebookstore.repository.RefreshTokenRepository;
import com.onlinebookstore.repository.UserRepository;
import com.onlinebookstore.service.AuthService;
import com.onlinebookstore.service.CookieService;
import com.onlinebookstore.service.AppUserService;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AppUserService userService;

	@Autowired
	private AuthService authService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private CookieService cookieService;

	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

		Authentication authenticate = authenticate(loginRequest);

		AppUser user = userRepository.findByUserEmail(loginRequest.email());

		// this is usefull for admin
//			if(user.isEnable()) {
//				throw new  DisabledException("User is desable");
//			}

		String jti = UUID.randomUUID().toString();

		RefreshToken token = new RefreshToken();
		token.setJti(jti);
		token.setUser(user);
		token.setCreatedAt(Instant.now());
		token.setExpireAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()));
		token.setRevoked(false);

		refreshTokenRepository.save(token);

		String accessToken = jwtService.generateAccessToken(user);
		String refreshToken = jwtService.generateRefreshToken(user, token.getJti());

		// Cookie section for add cookie service

		cookieService.createRefreshTokenCookie(response, refreshToken, (int) jwtService.getRefreshTtlSeconds());

		TokenResponse tokenResponse = TokenResponse.of(accessToken, refreshToken, jwtService.getAccessTtlSeconds(),
				accessToken, userService.mapToResponse(user));

		return ResponseEntity.ok(tokenResponse);

	}

	private Authentication authenticate(LoginRequest loginRequest) {

		try {

			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));

		} catch (Exception e) {
			throw new BadCredentialsException("Invalid User or Password");
		}

	}

	@PostMapping("/refresh")
	public ResponseEntity<TokenResponse> refreshToken(@RequestBody(required = false) RefreshTokenRequest body,
			HttpServletResponse response, HttpServletRequest request) {

		String refreshToken = readRefreshTokenFromRequest(body, request)
				.orElseThrow(() -> new BadCredentialsException("Refresh token is missing"));

		if (!jwtService.isRefreshToken(refreshToken)) {
			throw new BadCredentialsException("Invalid Refresh Token Understand");
		}

		String jti = jwtService.getJti(refreshToken);
		UUID userid = jwtService.getUserId(refreshToken);
		RefreshToken storedRefreshToken = refreshTokenRepository.findByJti(jti);
		if (storedRefreshToken == null) {
			throw new BadCredentialsException("Refres Token Are Not Recognized");
		}

		if (storedRefreshToken.isRevoked()) {
			throw new BadCredentialsException("Refresh Token is revoked");
		}

		if (storedRefreshToken.getExpireAt().isBefore(Instant.now())) {
			throw new BadCredentialsException("Refresh Token is Expired");
		}
		if (!storedRefreshToken.getUser().getUserId().equals(userid)) {
			throw new BadCredentialsException("Refresh Token does not belongs to this user");
		}

		// Refresh token ko Rotaed for Production Purpose its imp
		storedRefreshToken.setRevoked(true);
		String newJti = UUID.randomUUID().toString();
		storedRefreshToken.setReplacedByToken(newJti);
		refreshTokenRepository.save(storedRefreshToken);

		AppUser user = storedRefreshToken.getUser();

		RefreshToken newRefreshToken = new RefreshToken();
		newRefreshToken.setJti(newJti);
		newRefreshToken.setUser(user);
		newRefreshToken.setCreatedAt(Instant.now());
		newRefreshToken.setExpireAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()));
		newRefreshToken.setRevoked(false);

		refreshTokenRepository.save(newRefreshToken);

		String newAccessToken = jwtService.generateAccessToken(user);
		String newRefreshTokenOb = jwtService.generateRefreshToken(user, newRefreshToken.getJti());

		cookieService.createRefreshTokenCookie(response, newRefreshTokenOb, (int) jwtService.getRefreshTtlSeconds());

		return ResponseEntity.ok(TokenResponse.of(newAccessToken, newRefreshTokenOb,
				(int) jwtService.getAccessTtlSeconds(), null, userService.mapToResponse(user)));

	}

	private Optional<String> readRefreshTokenFromRequest(RefreshTokenRequest body, HttpServletRequest request) {

		if (request.getCookies() != null) {
			Optional<String> fromCookie = Arrays.stream(request.getCookies())
					.filter(c -> cookieService.getRefreshTokenCookieName().equals(c.getName())).map(Cookie::getValue)
					.filter(v -> !v.isBlank()).findFirst();

			if (fromCookie.isPresent()) {
				return fromCookie;
			}
		}

		// body
		if (body != null && body.refreshToken() != null && !body.refreshToken().isBlank()) {
			return Optional.of(body.refreshToken());
		}

		// 3 Custom Header
		String refreshHeader = request.getHeader("X-Refresh-Token");
		if (refreshHeader != null && !refreshHeader.isBlank()) {
			return Optional.of(refreshHeader.trim());
		}
		// Authorization = Bearer :Token
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String candidate = authHeader.substring(7).trim();

			if (!candidate.isEmpty()) {
				try {
					if (jwtService.isRefreshToken(candidate)) {
						return Optional.of(candidate);
					}
				} catch (Exception e) {

				}
			}

		}
		return Optional.empty();

	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {

		String refreshToken = readRefreshTokenFromRequest(null, request).orElse(null);

		if (refreshToken != null) {
			try {
				if (jwtService.isRefreshToken(refreshToken)) {

					String jti = jwtService.getJti(refreshToken);

					RefreshToken dbToken = refreshTokenRepository.findByJti(jti);

					if (dbToken != null) {
						dbToken.setRevoked(true);
						refreshTokenRepository.save(dbToken);
					}
				}
			} catch (JwtException e) {
				// ignore invalid token
			}
		}

		cookieService.deleteRefreshTokenCookie(response);
		SecurityContextHolder.clearContext();

		return ResponseEntity.noContent().build();
	}

	@PostMapping("/register")
	public UserResponseDTO register(@RequestBody UserRegisterRequest userRegister) {

		return authService.register(userRegister);
	}

}
