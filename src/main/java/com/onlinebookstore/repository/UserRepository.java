package com.onlinebookstore.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.onlinebookstore.entity.AppUser;

@Repository
public interface UserRepository extends JpaRepository<AppUser, UUID> {

	public List<AppUser> findAll();

	public AppUser findByUserEmail(String userEmail);

	public boolean existsByUserEmail(String string);
	
	List<AppUser> findTop5ByOrderByCreatedAtDesc();
	
	

}
