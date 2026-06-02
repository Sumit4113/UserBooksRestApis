package com.onlinebookstore.controller;

import java.util.List;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.onlinebookstore.dto.BookDto;
import com.onlinebookstore.dto.BookDtoRequest;
import com.onlinebookstore.service.BookService;

@RestController
@RequestMapping("/adminBook")
public class AdminBookController {

	@Autowired
	public BookService bookService;

	@PostMapping(value = "/addnew", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<BookDto> saveBook(@RequestParam("image") MultipartFile imageFile,
			@RequestParam("pdf") MultipartFile pdfFile, @ModelAttribute BookDtoRequest bookDto) {
		
		System.out.println("CONTROLLER HIT");
		
		BookDto savedBook = bookService.saveBookMethod(bookDto, imageFile, pdfFile);
		
		return ResponseEntity.ok(savedBook);
	}

	@GetMapping("/books")
	public ResponseEntity<List<BookDto>> getAllBooks() {
		List<BookDto> books = bookService.getAllBooks();
		return ResponseEntity.ok(books);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteBook(@PathVariable UUID id) {

		bookService.deletebooks(id);

		return ResponseEntity.ok("book deleted succfully");

	}

}
