package com.zun.freeboard.controller.post;

import static com.freeboard.domain.post.PostType.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.List;
import java.util.stream.IntStream;

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
import com.freeboard.controller.post.PostController;
import com.zun.freeboard.config.CustomMockUser;
import com.freeboard.domain.post.Post;
import com.freeboard.domain.post.PostType;
import com.freeboard.domain.user.User;
import com.freeboard.repository.user.UserRepository;
import com.freeboard.request.post.PostCreateRequest;
import com.freeboard.response.post.PostResponse;
import com.freeboard.service.post.PostService;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(PostController.class)
class PostControllerTest {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@MockBean
	private PostService postService;

	@MockBean
	private UserRepository userRepository;

	@DisplayName("게시글을 생성한다.")
	@CustomMockUser
	@Test
	void createPost() throws Exception {
		// given
		PostCreateRequest request = PostCreateRequest.builder()
			.postType(FREE)
			.title("제목입니다.")
			.content("내용입니다.")
			.build();

		String json = objectMapper.writeValueAsString(request);

		// when, then
		mockMvc.perform(
			MockMvcRequestBuilders.post("/api/posts")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf())
		)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@DisplayName("게시글 생성 시 게시글 타입은 필수 입력 필드이다.")
	@CustomMockUser
	@Test
	void createPostWithOutType() throws Exception{
		// given
		PostCreateRequest request = PostCreateRequest.builder()
			.title("제목입니다.")
			.content("제목입니다.")
			.build();

		String json = objectMapper.writeValueAsString(request);

		//when //then
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/posts")
					.content(json)
					.contentType(MediaType.APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@DisplayName("게시글을 전체 조회한다.")
	@CustomMockUser //todo 실제로는 인증이 필요없는데도 테스트 통과를 위해서 인증 처리를 해줘야 하는 것인가?
	@Test
	void getAllPosts() throws Exception {
		// given
		User user = User.builder()
			.name("zun")
			.email("zun@test.com")
			.nickname("zunny")
			.build();

		List<Post> posts = IntStream
			.range(1, 5)
			.mapToObj(i -> createPost(user, FREE, i + "번 제목입니다.", i + "번 내용입니다."))
			.toList();

		List<PostResponse> response = changePostListToPostResponseList(posts);

		// when
		when(postService.getAllPost(any())).thenReturn(response);

		//then
		mockMvc.perform(
				MockMvcRequestBuilders.get("/api/posts")
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OK"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
			.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@DisplayName("게시글 단건 조회한다.")
	@CustomMockUser
	@Test
	void getOnePost() throws Exception {
		// given
		User user = User.builder()
			.name("zun")
			.email("zun@test.com")
			.nickname("zunny")
			.build();

		Post post = createPost(user, FREE, "제목입니다.", "내용입니다.");
		ReflectionTestUtils.setField(post, "id", 1L);

		PostResponse response = PostResponse.of(post);

		// when
		when(postService.getOnePost(any())).thenReturn(response);

		// then
		mockMvc.perform(
				MockMvcRequestBuilders.get("/api/posts/{id}", 1)
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.postType").value("FREE"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("제목입니다."))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value("내용입니다."))
			.andExpect(MockMvcResultMatchers.status().isOk());

	}

	private Post createPost(User user, PostType postType, String title, String content) {
		Post post = Post.of(postType, title, content);
		post.setUser(user);
		return post;
	}

	private List<PostResponse> changePostListToPostResponseList(List<Post> posts) {
		return posts.stream()
			.map(PostResponse::of)
			.toList();
	}

}