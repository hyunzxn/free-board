package com.freeboard.service.auth;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.freeboard.domain.user.User;
import com.freeboard.domain.user.UserRole;
import com.freeboard.exception.AlreadyExistException;
import com.freeboard.repository.user.UserRepository;
import com.freeboard.request.auth.SignUpRequest;
import com.freeboard.service.auth.AuthService;

@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest {

	@BeforeEach
	void tearDown() {
		userRepository.deleteAllInBatch();
	}

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthService authService;

	@DisplayName("회원가입 한다.")
	@Test
	void signUp() {
		// given
		User user = User.of("Tom", "tom@test.com", "12345", "tommy", UserRole.USER);
		userRepository.save(user);

		SignUpRequest request = SignUpRequest.builder()
			.name("Zun")
			.email("zun@test.com")
			.password("12345")
			.nickname("zunny")
			.build();

		// when
		authService.signUp(request);

		// then
		List<User> users = userRepository.findAll();
		assertThat(users).hasSize(2)
			.extracting("name", "email", "nickname")
			.containsExactlyInAnyOrder(
				tuple("Tom", "tom@test.com", "tommy"),
				tuple("Zun", "zun@test.com", "zunny")
			);
	}

	@DisplayName("중복된 이메일로는 회원가입을 할 수 없다.")
	@Test
	void signUpFailByExistEmail() {
		// given
		User user = User.of("Tom", "tom@test.com", "12345", "tommy", UserRole.USER);
		userRepository.save(user);

		SignUpRequest request = SignUpRequest.builder()
			.name("Tomson")
			.email("tom@test.com")
			.password("12345")
			.nickname("tomtom")
			.build();

		// when, then
		assertThatThrownBy(() -> authService.signUp(request))
			.isInstanceOf(AlreadyExistException.class)
			.hasMessage("이미 존재하는 이메일입니다.");
	}

	@DisplayName("중복된 닉네임으로는 회원가입을 할 수 없다.")
	@Test
	void signUpFailByExistNickName() {
		// given
		User user = User.of("Tom", "tom@test.com", "12345", "tommy", UserRole.USER);
		userRepository.save(user);

		SignUpRequest request = SignUpRequest.builder()
			.name("Tomson")
			.email("tomson@test.com")
			.password("12345")
			.nickname("tommy")
			.build();

		// when, then
		assertThatThrownBy(() -> authService.signUp(request))
			.isInstanceOf(AlreadyExistException.class)
			.hasMessage("이미 존재하는 닉네임입니다.");
	}


}