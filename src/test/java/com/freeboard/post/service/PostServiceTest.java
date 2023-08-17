package com.freeboard.post.service;

import static com.freeboard.post.domain.PostType.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.freeboard.post.domain.Post;
import com.freeboard.post.domain.PostType;
import com.freeboard.user.domain.User;
import com.freeboard.user.domain.UserRole;
import com.freeboard.post.repository.PostRepository;
import com.freeboard.post.service.PostService;
import com.freeboard.user.repository.UserRepository;
import com.freeboard.post.controller.request.PostCreateRequest;
import com.freeboard.post.controller.request.PostSearch;
import com.freeboard.post.controller.request.PostUpdateRequest;
import com.freeboard.post.controller.response.PostResponse;

@ActiveProfiles("test")
@SpringBootTest
class PostServiceTest {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostService postService;

	@AfterEach
	void tearDown() {
		postRepository.deleteAllInBatch();
	}

	@DisplayName("게시글을 생성한다.")
	@Test
	void createPost() {
		// given
		User user = User.of("zun", "zun@gmail.com", "12345", "zunny", UserRole.USER);
		userRepository.save(user);

		PostCreateRequest request = PostCreateRequest.builder()
			.postType(FREE)
			.title("게시글 4번")
			.content("게시글 4번 내용")
			.build();

		// when
		PostResponse postResponse = postService.createPost(user.getId(), request.toServiceRequest());

		// then
		assertThat(postResponse.getId()).isNotNull();
		assertThat(postResponse)
			.extracting("postType", "title", "content", "writer")
			.contains(FREE, "게시글 4번", "게시글 4번 내용", "zunny");
	}

	@DisplayName("전체 게시글을 조회한다.")
	@Test
	void getAllPost() {
		// given
		User user = User.of("zun", "zun@gmail.com", "12345", "zunny", UserRole.USER);
		userRepository.save(user);

		Post post1 = createPost(user, FREE, "게시글 1번", "게시글 1번 내용");
		Post post2 = createPost(user, ASK, "게시글 2번", "게시글 2번 내용");
		Post post3 = createPost(user, INFORMATION, "게시글 3번", "게시글 3번 내용");
		postRepository.saveAll(List.of(post1, post2, post3));

		PostSearch postSearch = PostSearch.builder().build();

		// when
		List<PostResponse> posts = postService.getAllPost(postSearch);

		// then
		assertThat(posts).hasSize(3)
			.extracting("postType", "title", "content")
			.containsExactlyInAnyOrder(
				tuple(FREE, "게시글 1번", "게시글 1번 내용"),
				tuple(ASK, "게시글 2번", "게시글 2번 내용"),
				tuple(INFORMATION, "게시글 3번", "게시글 3번 내용")
			);
	}

	@DisplayName("게시글 단건 조회한다.")
	@Test
	void getOnePost() {
		// given
		User user = User.of("zun", "zun@gmail.com", "12345", "zunny", UserRole.USER);
		userRepository.save(user);

		Post post = createPost(user, FREE, "게시글 1번", "게시글 1번 내용");
		postRepository.save(post);

		// when
		PostResponse postResponse = postService.getOnePost(post.getId());

		// then
		assertThat(postResponse)
			.extracting("title", "content")
			.contains("게시글 1번", "게시글 1번 내용");
	}

	@DisplayName("게시글을 수정한다.")
	@Test
	void updatePost() {
		// given
		User user = User.of("zun", "zun@gmail.com", "12345", "zunny", UserRole.USER);
		userRepository.save(user);

		Post post = createPost(user, FREE, "게시글 1번", "게시글 1번 내용");
		postRepository.save(post);

		PostUpdateRequest request = PostUpdateRequest.builder()
			.title("게시글 1번 수정 후")
			.content("게시글 1번 내용 수정 후")
			.build();

		// when
		PostResponse postResponse = postService.updatePost(post.getId(), request);

		// then
		assertThat(postResponse)
			.extracting("title", "content")
			.contains("게시글 1번 수정 후", "게시글 1번 내용 수정 후");
	}

	@DisplayName("게시글을 삭제한다.")
	@Test
	void deletePost() {
		// given
		User user = User.of("zun", "zun@gmail.com", "12345", "zunny", UserRole.USER);
		userRepository.save(user);

		Post post = createPost(user, FREE, "게시글 1번", "게시글 1번 내용");
		postRepository.save(post);

		// when
		postService.deletePost(post.getId());

		// then
		assertThat(postRepository.count()).isEqualTo(0L);
	}

	private Post createPost(User user, PostType postType, String title, String content) {
		Post post = Post.of(postType, title, content);
		post.setUser(user);
		return post;
	}
}