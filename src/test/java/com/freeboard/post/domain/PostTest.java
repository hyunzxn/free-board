package com.freeboard.post.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.freeboard.post.domain.Post;
import com.freeboard.post.domain.PostType;
import com.freeboard.post.controller.request.PostUpdateRequest;

class PostTest {

	@DisplayName("Post 정보 업데이트한다.")
	@Test
	void update() {
		// given
		Post post = Post.of(PostType.FREE, "제목입니다.", "내용입니다.");

		PostUpdateRequest request = PostUpdateRequest.builder()
			.postType(PostType.ASK)
			.title("수정한 제목입니다.")
			.content("수정한 내용입니다.")
			.build();

		// when
		post.update(request);

		// then
		assertThat(post.getPostType()).isEqualByComparingTo(PostType.ASK);
		assertThat(post.getTitle()).isEqualTo("수정한 제목입니다.");
		assertThat(post.getContent()).isEqualTo("수정한 내용입니다.");
	}

}