package com.freeboard.request.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginRequest {

	private final String email;
	private final String password;

	@Builder
	public LoginRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
