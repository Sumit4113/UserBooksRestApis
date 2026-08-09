package com.onlinebookreader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.onlinebookreader.dto.BookDto;
import com.onlinebookreader.entity.BookAdd;
import com.onlinebookreader.service.WatchlistService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/watchlist")
public class WatchlistController {

    @Autowired
    private WatchlistService watchlistService;

    @PostMapping("/{bookId}")
    public void addToWatchlist(@PathVariable UUID bookId, Principal principal) {

        UUID userId = UUID.fromString(principal.getName());

        watchlistService.addToWatchlist(userId, bookId);
    }

    @GetMapping
    public List<BookDto> getWatchlist(Principal principal) {

        UUID userId = UUID.fromString(principal.getName());

        return watchlistService.getUserWatchlist(userId);
    }
}