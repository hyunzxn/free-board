package com.freeboard.request.post;

import com.freeboard.domain.post.PostType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCreateRequest {

	@NotNull(message = "게시글 유형을 선택해주세요.")
	private PostType postType;

	@NotBlank(message = "게시글 제목을 입력해주세요.")
	private String title;

	@NotBlank(message = "게시글 내용을 입력해주세요.")
	private String content;

	@Builder
	private PostCreateRequest(PostType postType, String title, String content) {
		this.postType = postType;
		this.title = title;
		this.content = content;
	}

	public PostCreateServiceRequest toServiceRequest() {
		return PostCreateServiceRequest.builder()
			.postType(postType)
			.title(title)
			.content(content)
			.build();
	}

}
