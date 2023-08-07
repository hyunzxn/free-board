package com.freeboard.response.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.freeboard.domain.post.Post;
import com.freeboard.domain.post.PostType;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponse {

	private final Long id;
	private final PostType postType;
	private final String title;
	private final String content;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final String writer;

	@Builder
	private PostResponse(Long id, PostType postType, String title, String content, String writer) {
		this.id = id;
		this.postType = postType;
		this.title = title;
		this.content = content;
		this.writer = writer;
	}

	public static PostResponse of(Post post) {
		return PostResponse.builder()
			.id(post.getId())
			.postType(post.getPostType())
			.title(post.getTitle())
			.content(post.getContent())
			.writer(post.getUser() != null ? post.getUser().getNickname() : null)
			.build();
	}

}
