package com.onlinebookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinebookstore.dto.BookDto;
import com.onlinebookstore.entity.*;
import com.onlinebookstore.repository.*;

import java.util.List;
import java.util.UUID;

@Service
public class WatchlistService {

    @Autowired
    private WatchlistRepository watchlistRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BookRepository bookRepo;
    
    private BookService bookService;

    public void addToWatchlist(UUID userId, UUID bookId) {

        // 🔥 prevent duplicate
    	boolean exists = watchlistRepo.existsByUser_UserIdAndBook_BookId(userId, bookId);

        if (exists) {
            throw new RuntimeException("Book already in watchlist");
        }


        AppUser user = userRepo.findById(userId).orElseThrow();
        BookAdd book = bookRepo.findById(bookId).orElseThrow();

        Watchlist w = new Watchlist();
        w.setUser(user);
        w.setBook(book);

        watchlistRepo.save(w);
    }

    public List<BookDto> getUserWatchlist(UUID userId) {
    	return watchlistRepo.findByUser_UserId(userId)
    	        .stream()
    	        .map(Watchlist::getBook)
    	        .map(this::mapToDto)
    	        .toList();
    }
    
    private BookDto mapToDto(BookAdd book) {

        BookDto dto = new BookDto();

        dto.setId(book.getBookId().toString());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setBookImage(book.getBookImage());

        return dto;
    }
}