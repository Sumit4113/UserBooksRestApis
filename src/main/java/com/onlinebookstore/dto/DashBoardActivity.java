package com.onlinebookstore.dto;

import java.time.LocalDateTime;

public class DashBoardActivity {

	private String message;
	
	private LocalDateTime time;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	
	
	
}
