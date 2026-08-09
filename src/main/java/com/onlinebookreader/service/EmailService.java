package com.onlinebookreader.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Value("${app.frontend.url}")
	private String frontendUrl;

	public void sendPasswordResetMeasage(String toEmail, String token) {

		String resetLink = frontendUrl + "/reset-password?token=" + token;

		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(toEmail);
		message.setSubject("Reset Password Message");
		message.setText("Hello,\n\n" + "We received a request to reset your password.\n\n" + "Click the link below:\n"
				+ resetLink + "\n\nThis link expires in 15 minutes.");

		mailSender.send(message);

	}

	public void sendWelcomeEmail(String email, String username) {

		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(email);
		message.setSubject("🎉 Welcome to Online Book Reader");

		message.setText("Hello " + username + ",\n\n" + "Welcome to Online Book Reader!\n\n"
				+ "Your account has been created successfully.\n\n" + "You can now:\n" + "• Browse books\n"
				+ "• Read free books\n" + "• Purchase premium books\n" + "• Create your watchlist\n\n"
				+ "Happy Reading! 📚\n\n" + "Team Online Book Reader");

		mailSender.send(message);
	}

}
