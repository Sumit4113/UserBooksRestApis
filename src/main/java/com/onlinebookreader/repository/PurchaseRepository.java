package com.onlinebookreader.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlinebookreader.entity.AppUser;
import com.onlinebookreader.entity.BookAdd;
import com.onlinebookreader.entity.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {

    boolean existsByUserAndBook(AppUser user, BookAdd book);
}