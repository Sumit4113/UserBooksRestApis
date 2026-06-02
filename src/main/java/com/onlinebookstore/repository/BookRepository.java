package com.onlinebookstore.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlinebookstore.entity.BookAdd;

@Repository
public interface BookRepository extends JpaRepository<BookAdd, UUID> {

	List<BookAdd> findByTitleContainingIgnoreCase(String keyword);

	List<BookAdd> findByGenreIgnoreCase(String genre);

//   this is also use for search funtionality with author , title, genre 	
//	@Query("""
//			SELECT b
//			FROM BookAdd b
//			WHERE LOWER(b.title)
//			LIKE LOWER(CONCAT('%', :keyword, '%'))
//
//			OR LOWER(b.author)
//			LIKE LOWER(CONCAT('%', :keyword, '%'))
//
//			OR LOWER(b.genre)
//			LIKE LOWER(CONCAT('%', :keyword, '%'))
//			""")
//			List<BookAdd> searchBooks(
//			        @Param("keyword")
//			        String keyword
//			);

}
