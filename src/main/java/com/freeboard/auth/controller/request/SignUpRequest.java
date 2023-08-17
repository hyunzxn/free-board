package com.freeboard.auth.controller.request;

import com.freeboard.user.domain.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpRequest {

	private final String name;
	private final String email;
	private final String password;
	private final String nickname;

	@Builder
	public SignUpRequest(String name, String email, String password, String nickname) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.nickname = nickname;
	}

	public User toEntity() {
		return User.builder()
			.name(name)
			.email(email)
			.password(password)
			.nickname(nickname)
			.build();
	}
}
