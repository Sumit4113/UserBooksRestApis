package com.onlinebookstore.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlinebookstore.entity.BookAdd;

@Repository
public interface BookRepository extends JpaRepository<BookAdd, UUID> {
	
	

}
