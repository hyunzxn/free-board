package com.freeboard.post.service.request;

import com.freeboard.post.domain.PostType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCreateServiceRequest {

	private PostType postType;
	private String title;
	private String content;

	@Builder
	private PostCreateServiceRequest(PostType postType, String title, String content) {
		this.postType = postType;
		this.title = title;
		this.content = content;
	}

}
