package com.freeboard.config.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

public class CustomEmailPasswordFilter extends AbstractAuthenticationProcessingFilter {

	private static final String LOGIN_URL = "/api/auth/login";

	private final ObjectMapper objectMapper;

	public CustomEmailPasswordFilter(ObjectMapper objectMapper) {
		super(LOGIN_URL);
		this.objectMapper = objectMapper;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException,
		IOException,
		ServletException {
		EmailPassword emailPassword = objectMapper.readValue(request.getInputStream(), EmailPassword.class);

		// 인증 대상 객체
		UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
			emailPassword.email,
			emailPassword.password
		);

		token.setDetails(this.authenticationDetailsSource.buildDetails(request));
		return this.getAuthenticationManager().authenticate(token);
	}

	@Getter
	private static class EmailPassword {
		private String email;
		private String password;
	}
}
