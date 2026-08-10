package com.onlinebookreader.service;

import java.util.Calendar;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onlinebookreader.entity.AppUser;
import com.onlinebookreader.entity.PasswordReset;
import com.onlinebookreader.repository.PasswordResetRepository;
import com.onlinebookreader.repository.UserRepository;

@Service
public class PasswordResetService {

	@Autowired
	private UserRepository userRpo;

	@Autowired
	private PasswordResetRepository passwordResetRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private PasswordEncoder encoder;

	public void createPasswordResetToken(String email) {

		AppUser user = userRpo.findByUserEmail(email);

		if (user == null) {
			return;
		}

		PasswordReset reset = passwordResetRepository.findByUser(user);

		if (reset != null) {
			passwordResetRepository.delete(reset);
		}

		String token = UUID.randomUUID().toString();

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 15);
		Date expiry = calendar.getTime();

		PasswordReset password = new PasswordReset();

		password.setExpiryTime(expiry);
		password.setToken(token);
		password.setUser(user);

		passwordResetRepository.save(password);

		emailService.sendPasswordResetMeasage(user.getUserEmail(), token);
	}

	public void resetPassword(String token, String newPassword) {

		PasswordReset passwordReset = passwordResetRepository.findByToken(token);

		if (passwordReset == null) {

			throw new IllegalArgumentException("Invalid password reset token");

		}

		if (passwordReset.getExpiryTime().before(new Date())) {

			passwordResetRepository.delete(passwordReset);

			throw new IllegalArgumentException("Password reset token expire");

		}

		AppUser user = passwordReset.getUser();

		String encodedPassword = encoder.encode(newPassword);

		System.out.println("ENCODER CLASS = "
		        + encoder.getClass().getName());

		System.out.println("ENCODED NULL = "
		        + (encodedPassword == null));

		System.out.println("ENCODED LENGTH = "
		        + (encodedPassword == null ? 0 : encodedPassword.length()));
		
		user.setUserPassword(encodedPassword);

		userRpo.save(user);

		passwordResetRepository.delete(passwordReset);
	}

}
