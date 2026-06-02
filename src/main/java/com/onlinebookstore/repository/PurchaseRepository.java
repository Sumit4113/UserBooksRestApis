package com.onlinebookstore.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlinebookstore.entity.AppUser;
import com.onlinebookstore.entity.BookAdd;
import com.onlinebookstore.entity.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {

    boolean existsByUserAndBook(AppUser user, BookAdd book);
}