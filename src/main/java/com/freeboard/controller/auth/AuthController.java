package com.freeboard.controller.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freeboard.service.auth.AuthService;
import com.freeboard.request.auth.SignUpRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class AuthController {

	private final AuthService authService;

	@PostMapping("/auth/sign-up")
	public String signUp(@RequestBody SignUpRequest request) {
		authService.signUp(request);
		return "회원가입 성공";
	}
}
