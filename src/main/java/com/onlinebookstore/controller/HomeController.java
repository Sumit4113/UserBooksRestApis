package com.onlinebookstore.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlinebookstore.dto.BookDto;
import com.onlinebookstore.entity.BookAdd;
import com.onlinebookstore.repository.BookRepository;
import com.onlinebookstore.service.BookService;
import com.onlinebookstore.service.PaymentService;

@RestController
@RequestMapping("/books")
public class HomeController {

	@Autowired
	private BookService bookService;

	@Autowired
	private BookRepository bookRepo;

	@Autowired
	private PaymentService paymentService;

	@GetMapping("/getAllBooks")
	public List<BookDto> getAllBooks() {

		return bookService.getAllBooks();
	}

	@GetMapping("/search")
	public ResponseEntity<List<BookDto>> searchBooks(@RequestParam String keyword) {

		return ResponseEntity.ok(bookService.searchBooks(keyword));
	}

	@GetMapping("/{id}")
	public ResponseEntity<BookDto> getBookById(@PathVariable UUID id) {
		BookDto book = bookService.getBookById(id);
		return ResponseEntity.ok(book);
	}

	@GetMapping("/genre/{genre}")
	public List<BookAdd> getBooksByGenre(@PathVariable String genre) {

		return bookRepo.findByGenreIgnoreCase(genre);
	}

	@GetMapping("/pdf/{id}")
	public ResponseEntity<byte[]> getPdf(@PathVariable UUID id, Authentication authentication) {

		try {

			// Load Book
			BookAdd book = bookRepo.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));

			/*
			 * FREE BOOK
			 */
			if ("FREE".equalsIgnoreCase(book.getMode())) {

				byte[] pdf = bookService.loadPdf(book);

				return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(pdf);
			}

			/*
			 * PAID BOOK
			 */

			// User not logged in
			if (authentication == null) {

				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

			}

			UUID userId = UUID.fromString(authentication.getName());

			boolean purchased = paymentService.hasPurchased(userId, id);

			if (!purchased) {

				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

			}

			byte[] pdf = bookService.loadPdf(book);

			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(pdf);

		}

		catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.notFound().build();

		}

	}

}
