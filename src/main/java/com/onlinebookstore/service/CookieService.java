package com.onlinebookstore.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CookieService {

	private final String refreshTokenCookieName;
	private final boolean cookieHttpOnly;
	private final boolean cookieSecure;
	private final String cookieDomain;
	private final String cookieSameSite;

	public CookieService(@Value("${jwt.cookie.name}") String refreshTokenCookieName,
			@Value("${jwt.cookie.http.only}") boolean cookieHttpOnly,
			@Value("${jwt.cookie.secure}") boolean cookieSecure,
			@Value("${jwt.cookie.same.site}") String cookieSameSite,
			@Value("${jwt.cookie.domain}") String cookieDomain) {
		super();
		this.refreshTokenCookieName = refreshTokenCookieName;
		this.cookieHttpOnly = cookieHttpOnly;
		this.cookieSecure = cookieSecure;
		this.cookieDomain = cookieDomain;
		this.cookieSameSite = cookieSameSite;
	}

	public void createRefreshTokenCookie( HttpServletResponse response, String value, int maxAge) {

		var responseCookieBuilder = ResponseCookie
				.from(refreshTokenCookieName, value)
				.httpOnly(cookieHttpOnly)
				.secure(cookieSecure)
				.path("/")
				.maxAge(maxAge)
				.sameSite(cookieSameSite);

		if (cookieDomain != null && !cookieDomain.isBlank()) {
			responseCookieBuilder.domain(cookieDomain);
		}

		ResponseCookie responseCookie = responseCookieBuilder.build();

		response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
		
	}

	public void deleteRefreshTokenCookie( HttpServletResponse response) {

		ResponseCookieBuilder cookielogout = ResponseCookie.
				from(refreshTokenCookieName, "")
				.maxAge(0)
				.httpOnly(cookieHttpOnly)
				.path("/")
				.sameSite(cookieSameSite)
				.secure(cookieSecure);

		if (cookieDomain != null && !cookieDomain.isBlank()) {
			cookielogout.domain(cookieDomain);
		}

		ResponseCookie responseCookie = cookielogout.build();

		response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

	}

	public String getRefreshTokenCookieName() {
		return refreshTokenCookieName;
	}

	public boolean isCookieHttpOnly() {
		return cookieHttpOnly;
	}

	public boolean isCookieSecure() {
		return cookieSecure;
	}

	public String getCookieDomain() {
		return cookieDomain;
	}

	public String getCookieSameSite() {
		return cookieSameSite;
	}

	

}
