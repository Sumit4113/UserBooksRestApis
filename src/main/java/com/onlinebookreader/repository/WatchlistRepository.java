package com.onlinebookreader.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlinebookreader.entity.AppUser;
import com.onlinebookreader.entity.Watchlist;

public interface WatchlistRepository extends JpaRepository<Watchlist, UUID> {

    List<Watchlist> findByUser_UserId(UUID userId);

    boolean existsByUser_UserIdAndBook_BookId(UUID userId, UUID bookId);
    
    List<Watchlist> findTop5ByOrderByCreatedAtDesc();
}
 	