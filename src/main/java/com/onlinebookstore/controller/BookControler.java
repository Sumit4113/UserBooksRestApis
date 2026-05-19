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
import com.onlinebookstore.service.BookService;

@RestController
@RequestMapping("/books")
public class BookControler {
	
	

	@Autowired
	private BookService bookService;

	@PostMapping(value = "/addnew", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<BookDto> saveBook(
	        @RequestParam("image") MultipartFile imageFile,
	        @RequestParam("pdf") MultipartFile pdfFile,
	        @ModelAttribute BookDto bookDto
	)
	{
	    BookDto savedBook = bookService.saveBookMethod(bookDto, imageFile, pdfFile);
	    return ResponseEntity.ok(savedBook);
	}


	@GetMapping("/getAllBooks")
	public List<BookDto> getAllBooks() {

		return bookService.getAllBooks();
	}

	@GetMapping("/{id}")
	public ResponseEntity<BookDto> getBookById(@PathVariable UUID id) {
	    BookDto	 book = bookService.getBookById(id);
	    return ResponseEntity.ok(book);
	}


}
