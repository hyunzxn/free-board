package com.zun.freeboard.config;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.freeboard.domain.user.User;
import com.freeboard.domain.user.UserPrincipal;
import com.freeboard.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomMockSecurityContext implements WithSecurityContextFactory<CustomMockUser> {

	private final UserRepository userRepository;

	@Override
	public SecurityContext createSecurityContext(CustomMockUser annotation) {
		var user = User.builder()
			.name(annotation.name())
			.email(annotation.email())
			.password(annotation.password())
			.build();
		userRepository.save(user);

		var principal = new UserPrincipal(user);

		var role = new SimpleGrantedAuthority("ROLE_ADMIN");
		var authenticationToken = new UsernamePasswordAuthenticationToken(principal, user.getPassword(), List.of(role));

		var context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authenticationToken);

		return context;
	}
}
