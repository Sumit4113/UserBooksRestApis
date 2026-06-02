package com.onlinebookstore.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
public class Watchlist {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "userId")
	private AppUser user;

	@ManyToOne
	@JoinColumn(name = "bookId")
	private BookAdd book;

	@Column
	private LocalDateTime createdAt;

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public BookAdd getBook() {
		return book;
	}

	public void setBook(BookAdd book) {
		this.book = book;
	}

}
