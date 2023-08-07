package com.freeboard.service.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freeboard.domain.user.User;
import com.freeboard.domain.user.UserRole;
import com.freeboard.exception.AlreadyExistException;
import com.freeboard.repository.user.UserRepository;
import com.freeboard.request.auth.SignUpRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void signUp(SignUpRequest request) {

		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new AlreadyExistException("이미 존재하는 이메일입니다.");
		}

		if (userRepository.findByNickname(request.getNickname()).isPresent()) {
			throw new AlreadyExistException("이미 존재하는 닉네임입니다.");
		}

		User user = User.builder()
			.name(request.getName())
			.email(request.getEmail())
			.password(request.getPassword())
			.nickname(request.getNickname())
			.role(UserRole.USER)
			.build();

		user.passwordEncode(passwordEncoder);
		userRepository.save(user);
	}
}
