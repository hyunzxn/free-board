package com.freeboard.user.domain;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class UserPrincipal extends User {

	private final Long userId;

	public UserPrincipal(com.freeboard.user.domain.User user) {
		super(
			user.getEmail(),
			user.getPassword(),
			List.of(new SimpleGrantedAuthority("ROLE_USER"))
		);
		this.userId = user.getId();
	}

}
