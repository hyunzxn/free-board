package com.freeboard.comment.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.freeboard.comment.controller.response.CommentResponse;
import com.freeboard.comment.domain.Comment;
import com.freeboard.comment.repository.CommentRepository;
import com.freeboard.comment.service.request.CommentCreateServiceRequest;
import com.freeboard.post.domain.Post;
import com.freeboard.post.domain.PostType;
import com.freeboard.post.repository.PostRepository;
import com.freeboard.user.domain.User;
import com.freeboard.user.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
class CommentServiceTest {

	@Autowired
	private CommentService commentService;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@AfterEach
	void tearDown() {
		commentRepository.deleteAllInBatch();
		postRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("댓글을 생성할 수 있다.")
	@Test
	void createComment() {
		// given
		User user = User.builder()
			.name("문현준")
			.email("jann1653@gmail.com")
			.nickname("zun")
			.build();
		userRepository.save(user);

		Post post = Post.builder()
			.postType(PostType.FREE)
			.title("햄버거 먹고 싶다.")
			.content("롯데리아 신메뉴 먹어보고 싶다.")
			.build();
		postRepository.save(post);

		CommentCreateServiceRequest request = CommentCreateServiceRequest.builder()
			.content("롯데리아 신메뉴 맛있습니다! 추천해요!")
			.password("12345")
			.build();

		// when
		CommentResponse result = commentService.create(user.getId(), post.getId(), request);

		// then
		assertThat(result.getAuthor()).isEqualTo("zun");
		assertThat(result.getContent()).isEqualTo("롯데리아 신메뉴 맛있습니다! 추천해요!");

	}

	@DisplayName("댓글 삭제할 수 있다.")
	@Test
	void deleteComment() {
		// given
		User user = User.builder()
			.name("문현준")
			.email("jann1653@gmail.com")
			.nickname("zun")
			.build();
		userRepository.save(user);

		Post post = Post.builder()
			.postType(PostType.FREE)
			.title("햄버거 먹고 싶다.")
			.content("롯데리아 신메뉴 먹어보고 싶다.")
			.build();
		postRepository.save(post);

		Comment comment = Comment.builder()
			.content("테스트용 댓글입니다.")
			.password("12345")
			.build();
		comment.setAuthor(user.getNickname());
		comment.setPost(post);
		commentRepository.save(comment);

		// when
		commentService.delete(comment.getId());

		// then
		List<Comment> result = commentRepository.findAll();
		assertThat(result).isEmpty();

	}

}