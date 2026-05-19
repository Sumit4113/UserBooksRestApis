package com.onlinebookstore.dto;

import java.time.OffsetDateTime;

public record ApiGlobalErrorRecord(
		int status, 
		String error, 
		String message, 
		String path, 
		OffsetDateTime time

) {
	public static ApiGlobalErrorRecord of(int status, String error, String message, String path, OffsetDateTime time) {

		return new ApiGlobalErrorRecord(status, error, message, path, time);
	}

}
