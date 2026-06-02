package com.onlinebookstore.repository;

import java.util.UUID;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.onlinebookstore.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

	public RefreshToken findByJti(String jti);

	@Modifying
	@Transactional
	@Query("""
			    DELETE FROM RefreshToken rt
			    WHERE rt.user.userId = :id
			""")
	void deleteByUser_UserId(@Param("id") UUID id);
	
	
}
