package com.freeboard.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserRole {

	ADMIN("ROLE_ADMIN"),
	USER("ROLE_USER"),
	GUEST("ROLE_GUEST");

	private final String text;

}
