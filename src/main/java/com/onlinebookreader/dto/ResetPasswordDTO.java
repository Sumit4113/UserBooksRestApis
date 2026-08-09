package com.onlinebookreader.dto;

public class ResetPasswordDTO {
	
	private String  token;
	
	private String newPaasword;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNewPaasword() {
		return newPaasword;
	}

	public void setNewPaasword(String newPaasword) {
		this.newPaasword = newPaasword;
	}
	
	

}
