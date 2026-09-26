package com.onlinebookreader.configuration;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.onlinebookreader.entity.AppUser;
import com.onlinebookreader.entity.RefreshToken;
import com.onlinebookreader.jwt.JwtService;
import com.onlinebookreader.repository.RefreshTokenRepository;
import com.onlinebookreader.service.AppUserService;
import com.onlinebookreader.service.CookieService;
import com.onlinebookreader.service.HelperMethod;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2Success implements AuthenticationSuccessHandler {

	private final CookieService cookieService;

	private final AppUserService service;

	private final RefreshTokenRepository repo;

	private final JwtService jwtService;

	public OAuth2Success(AppUserService service, JwtService jwtService, RefreshTokenRepository repo,
			HelperMethod helperMethod, CookieService cookieService) {
		super();
		this.cookieService = cookieService;
		this.service = service;
		this.repo = repo;
		this.jwtService = jwtService;

	}

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		logger.info("Success Authentication");
		logger.info(authentication.toString());

		OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

		String email = oauthUser.getAttribute("email");
		String name = oauthUser.getAttribute("name");

		AppUser user = service.findGoogleEmail(email, name);

		// Refresh token

		String jti = UUID.randomUUID().toString();

		RefreshToken tokenMade = new RefreshToken();

		tokenMade.setJti(jti);
		tokenMade.setUser(user);
		tokenMade.setCreatedAt(Instant.now());
		tokenMade.setExpireAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()));
		tokenMade.setRevoked(false);

		repo.save(tokenMade);

		String accessToken = jwtService.generateAccessToken(user);

		String refreshToken = jwtService.generateRefreshToken(user, jti);

		cookieService.createRefreshTokenCookie(response, refreshToken, (int) jwtService.getRefreshTtlSeconds());

		response.getWriter().write("Login Successfull");

		response.sendRedirect("http://localhost:5173/oauth-success?token=" + accessToken);

	}

}
