package com.onlinebookreader.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlinebookreader.entity.AppUser;
import com.onlinebookreader.entity.PasswordReset;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Integer> {

	PasswordReset findByToken(String token);

	PasswordReset findByUser(AppUser user);

	void deleteByUser(AppUser user);

}
