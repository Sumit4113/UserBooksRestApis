package com.onlinebookstore.service;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.onlinebookstore.dto.BookDto;
import com.onlinebookstore.entity.BookAdd;
import com.onlinebookstore.repository.BookRepository;

@Service
public class BookService {
	@Autowired
	private CloudiServiceImg cloudiServiceImg;

	@Autowired
	private BookRepository bookRepo;

	public BookDto saveBookMethod(BookDto bookDto, MultipartFile imageFile, MultipartFile pdfFile) {

		BookAdd book = new BookAdd();

		book.setTitle(bookDto.getTitle());
		book.setAuthor(bookDto.getAuthor());
		book.setDescription(bookDto.getDescription());
		book.setGenre(bookDto.getGenre());
		book.setPrice(bookDto.getPrice());
		book.setPublisher(bookDto.getPublisher());

		try {
			// ✅ Upload IMAGE to Cloudinary
			if (imageFile != null && !imageFile.isEmpty()) {
				String imageUrl = cloudiServiceImg.uploadImage(imageFile);
				book.setBookImage(imageUrl);
			}

			// ✅ Upload PDF to Cloudinary
			if (pdfFile != null && !pdfFile.isEmpty()) {
				String pdfUrl = cloudiServiceImg.uploadPdf(pdfFile);
				book.setPdf(pdfUrl);
			}

		} catch (Exception e) {
			throw new RuntimeException("Cloudinary upload failed", e);
		}

		BookAdd savedBook = bookRepo.save(book);

		return mapToDto(savedBook);
	}

	public List<BookDto> getAllBooks() {

		List<BookAdd> bookAdd = bookRepo.findAll();

		List<BookDto> bookDtoResponse = new ArrayList<BookDto>();

		for (BookAdd addBook : bookAdd) {

			BookDto book = mapToDto(addBook);

			bookDtoResponse.add(book);

		}

		return bookDtoResponse;

	}

	public BookAdd getBookById(UUID id) {
		return bookRepo.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
	}

	private BookDto mapToDto(BookAdd bookAdd) {
		BookDto dto = new BookDto();
		dto.setBookId(bookAdd.getBookId());
		dto.setTitle(bookAdd.getTitle());
		dto.setAuthor(bookAdd.getAuthor());
		dto.setPrice(bookAdd.getPrice());
		dto.setGenre(bookAdd.getGenre());
		dto.setBookImage(bookAdd.getBookImage());
		dto.setPdfUrl(bookAdd.getPdf());
		return dto;
	}

}
