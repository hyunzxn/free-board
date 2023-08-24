package com.freeboard.comment.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freeboard.comment.controller.request.CommentCreateRequest;
import com.freeboard.comment.domain.Comment;
import com.freeboard.comment.service.CommentService;
import com.freeboard.config.CustomMockUser;
import com.freeboard.post.domain.Post;
import com.freeboard.post.domain.PostType;
import com.freeboard.user.repository.UserRepository;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(CommentController.class)
class CommentControllerTest {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@MockBean
	private CommentService commentService;

	@MockBean
	private UserRepository userRepository; //CustomMockSecurityContext 때문에 계속 넣어줘야 하는 거 같은데 이게 맞나?


	@DisplayName("댓글을 생성할 수 있다.")
	@CustomMockUser
	@Test
	void createComment() throws Exception {
		// given
		Post post = Post.builder()
			.postType(PostType.FREE)
			.title("햄버거 먹고 싶다.")
			.content("롯데리아 신메뉴 먹고 싶다.")
			.build();
		ReflectionTestUtils.setField(post, "id", 1L);

		CommentCreateRequest request = CommentCreateRequest.builder()
			.content("테스트용 댓글입니다.")
			.password("12345")
			.build();

		String json = objectMapper.writeValueAsString(request);

		// when, then
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/posts/{postId}/comments", post.getId())
					.content(json)
					.contentType(MediaType.APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk());

	}

	@DisplayName("댓글을 삭제할 수 있다.")
	@CustomMockUser
	@Test
	void deleteComment() throws Exception {
		// given
		Comment comment = Comment.builder()
			.content("롯데리아 새우버거 맛있다.")
			.password("12345")
			.build();
		ReflectionTestUtils.setField(comment, "id", 1L);

		// when, then
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/api/comments/{commentId}", comment.getId())
					.with(csrf())
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk());

	}

}