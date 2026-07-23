package com.onlinebookstore.service;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import java.util.List;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.onlinebookstore.customexception.ResourceNotFoundException;
import com.onlinebookstore.dto.BookDto;
import com.onlinebookstore.dto.BookDtoRequest;
import com.onlinebookstore.entity.BookAdd;
import com.onlinebookstore.repository.BookRepository;

@Service
public class BookService {
	@Autowired
	private CloudiServiceImg cloudiServiceImg;

	@Autowired
	private BookRepository bookRepo;

	public BookDto saveBookMethod(BookDtoRequest bookDto, MultipartFile imageFile, MultipartFile pdfFile) {

		BookAdd book = new BookAdd();

		book.setTitle(bookDto.getTitle());
		book.setAuthor(bookDto.getAuthor());
		book.setDescription(bookDto.getDescription());
		book.setGenre(bookDto.getGenre());
		book.setPrice(bookDto.getPrice());
		book.setRating(bookDto.getRating());
		book.setPublisher(bookDto.getPublisher());
		book.setMode(bookDto.getMode());
		book.setNewDate(bookDto.getNewDate());

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

	public List<BookDto> searchBooks(String keyword) {

		List<BookAdd> books = bookRepo.findByTitleContainingIgnoreCase(keyword);

		List<BookDto> result = new ArrayList<>();

		for (BookAdd book : books) {
			result.add(mapToDto(book));
		}

		return result;
	}

	public BookDto getBookById(UUID id) {
		BookAdd book = bookRepo.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));

		return mapToDto(book);
	}

	private BookDto mapToDto(BookAdd bookAdd) {
		BookDto dto = new BookDto();
		dto.setId(bookAdd.getBookId().toString());
		dto.setTitle(bookAdd.getTitle());
		dto.setAuthor(bookAdd.getAuthor());
		dto.setPrice(bookAdd.getPrice());
		dto.setPublisher(bookAdd.getPublisher());
		dto.setGenre(bookAdd.getGenre());
		dto.setRating(bookAdd.getRating());
		dto.setDescription(bookAdd.getDescription());
		dto.setMode(bookAdd.getMode());
		dto.setNewDate(bookAdd.getNewDate());
		dto.setBookImage(bookAdd.getBookImage());
		dto.setBookPdf(bookAdd.getPdf());
		
		return dto;
	}

	public void deletebooks(UUID id) {
		BookAdd book = bookRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));

		bookRepo.delete(book);
	}
	
	public void pdfFile() {
		
	}

	public byte[] loadPdf(BookAdd book) {

	    try {

	        URL url = new URL(book.getPdf());

	        try (InputStream input = url.openStream()) {

	            return input.readAllBytes();

	        }

	    }

	    catch (Exception e) {

	        throw new RuntimeException("Unable to load PDF", e);

	    }

	}
	
}
