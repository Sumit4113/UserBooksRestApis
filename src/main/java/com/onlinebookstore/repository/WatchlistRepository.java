package com.onlinebookstore.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlinebookstore.entity.AppUser;
import com.onlinebookstore.entity.Watchlist;

public interface WatchlistRepository extends JpaRepository<Watchlist, UUID> {

    List<Watchlist> findByUser_UserId(UUID userId);

    boolean existsByUser_UserIdAndBook_BookId(UUID userId, UUID bookId);
    
    List<Watchlist> findTop5ByOrderByCreatedAtDesc();
}
 	