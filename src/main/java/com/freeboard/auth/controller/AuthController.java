package com.freeboard.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freeboard.auth.controller.request.SignUpRequest;
import com.freeboard.auth.service.AuthService;

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
