package com.freeboard.user.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.freeboard.common.domain.BaseEntity;
import com.freeboard.post.domain.Post;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
@Entity
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String email;

	private String password;

	private String nickname;

	@Enumerated(EnumType.STRING)
	private UserRole role;

	private String refreshToken;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Post> posts = new ArrayList<>();

	@Builder
	private User(String name, String email, String password, String nickname, UserRole role) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.role = role;
	}

	public static User of(String name, String email, String password, String nickName, UserRole role) {
		return User.builder()
			.name(name)
			.email(email)
			.password(password)
			.nickname(nickName)
			.role(role)
			.build();
	}

	// 유저 권한 설정 메소드
	public void authorizeUser() {
		this.role = UserRole.USER;
	}

	// 비밀번호 암호화 메소드
	public void passwordEncode(PasswordEncoder passwordEncoder) {
		this.password = passwordEncoder.encode(this.password);
	}

	public void updateRefreshToken(String updateRefreshToken) {
		this.refreshToken = updateRefreshToken;
	}
}
