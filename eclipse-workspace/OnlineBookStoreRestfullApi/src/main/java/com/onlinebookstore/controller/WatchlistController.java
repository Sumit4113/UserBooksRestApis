package com.onlinebookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.onlinebookstore.entity.BookAdd;
import com.onlinebookstore.service.WatchlistService;

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
    public List<BookAdd> getWatchlist(Principal principal) {

        UUID userId = UUID.fromString(principal.getName());

        return watchlistService.getUserWatchlist(userId);
    }
}