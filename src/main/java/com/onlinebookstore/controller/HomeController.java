package com.onlinebookstore.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.onlinebookstore.dto.BookDto;
import com.onlinebookstore.entity.BookAdd;
import com.onlinebookstore.repository.BookRepository;
import com.onlinebookstore.service.BookService;

@RestController
@RequestMapping("/books")
public class HomeController {

	@Autowired
	private BookService bookService;
	
	@Autowired
	private BookRepository bookRepo;

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

}
