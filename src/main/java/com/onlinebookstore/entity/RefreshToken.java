package com.onlinebookstore.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table( indexes = {

		@Index(name = "refresh_token_jti_idx", columnList = "jti", unique = true),
		@Index(name = "refresh_token_user_id_idx", columnList = "userid"),

})
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "jti", unique = true, nullable = false, updatable = false)
	private String jti;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "userid", nullable = false, updatable = false)
	private AppUser user;

	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	@Column(nullable = false)
	private Instant expireAt;

	@Column(nullable = false)
	private boolean revoked;

	private String replacedByToken;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getJti() {
		return jti;
	}

	public void setJti(String jti) {
		this.jti = jti;
	}

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getExpireAt() {
		return expireAt;
	}

	public void setExpireAt(Instant expireAt) {
		this.expireAt = expireAt;
	}

	public boolean isRevoked() {
		return revoked;
	}

	public void setRevoked(boolean revoked) {
		this.revoked = revoked;
	}

	public String getReplacedByToken() {
		return replacedByToken;
	}

	public void setReplacedByToken(String replacedByToken) {
		this.replacedByToken = replacedByToken;
	}

}
