package com.freeboard.auth.controller;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freeboard.user.repository.UserRepository;
import com.freeboard.auth.controller.request.SignUpRequest;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void tearDown() {
		userRepository.deleteAllInBatch();
	}

	@DisplayName("회원가입 한다.")
	@Test
	void signUp() throws Exception {
		//given
		SignUpRequest request = SignUpRequest.builder()
			.name("zun")
			.email("zun@test.com")
			.password("12345")
			.nickname("zunny")
			.build();

		//expected
		mockMvc.perform(post("/api/auth/sign-up")
				.content(objectMapper.writeValueAsString(request))
				.contentType(APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andDo(print());
	}

}