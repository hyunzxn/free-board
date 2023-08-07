package com.zun.freeboard.controller.auth;

import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freeboard.controller.auth.AuthController;
import com.freeboard.repository.user.UserRepository;
import com.freeboard.request.auth.SignUpRequest;
import com.freeboard.service.auth.AuthService;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
class AuthControllerTestWithWebMvcTest {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@MockBean
	private AuthService authService;

	@MockBean
	private UserRepository userRepository;


	@DisplayName("회원가입 한다.")
	@Test
	void signUp() throws Exception {
		// given
		SignUpRequest request = SignUpRequest.builder()
			.name("zun")
			.email("zun@test.com")
			.password("12345")
			.nickname("zunny")
			.build();

		// then
		mockMvc.perform(post("/api/auth/sign-up")
				.content(objectMapper.writeValueAsString(request))
				.contentType(APPLICATION_JSON)
				.with(csrf())
			)
			.andExpect(status().isOk())
			.andDo(print());
	}

}