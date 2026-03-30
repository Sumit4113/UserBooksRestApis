
package com.onlinebookstore.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.onlinebookstore.entity.AppUser;
import com.onlinebookstore.entity.RefreshToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey key;
    private final long accessTtlSeconds;
    private final long refreshTtlSeconds;
    private final String issuer;

    public JwtService(
            @Value("${jwt.key}") String secret,
            @Value("${access.ttl.seconds}") long accessTtlSeconds,
            @Value("${refresh.ttl.seconds}") long refreshTtlSeconds,
            @Value("${jwt.issuer}") String issuer) {

        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("JWT secret too short");
        }

        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTtlSeconds = accessTtlSeconds;
        this.refreshTtlSeconds = refreshTtlSeconds;
        this.issuer = issuer;
    }

    // ACCESS TOKEN
    public String generateAccessToken(AppUser user) {

        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(user.getUserId().toString())
                .setIssuer(issuer)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(accessTtlSeconds)))
                .claim("email", user.getUserEmail())
                .claim("role", user.getUserRole())
                .claim("typ", "access")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // REFRESH TOKEN
    public String generateRefreshToken(AppUser user, String jti) {

        Instant now = Instant.now();

        return Jwts.builder()
                .setId(jti)
                .setSubject(user.getUserId().toString())
                .setIssuer(issuer)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(refreshTtlSeconds)))
                .claim("typ", "refresh")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isAccessToken(String token) {
        return "access".equals(parse(token).get("typ"));
    }

    public boolean isRefreshToken(String token) {
        return "refresh".equals(parse(token).get("typ"));
    }

    public UUID getUserId(String token) {
        return UUID.fromString(parse(token).getSubject());
    }

	public SecretKey getKey() {
		return key;
	}

	public long getAccessTtlSeconds() {
		return accessTtlSeconds;
	}

	public long getRefreshTtlSeconds() {
		return refreshTtlSeconds;
	}

	public String getIssuer() {
		return issuer;
	}

	public String getJti(String token) {
	    return parse(token).getId();
	}


	
    
    
}
