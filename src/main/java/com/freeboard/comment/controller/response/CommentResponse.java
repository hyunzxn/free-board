package com.freeboard.comment.controller.response;

import com.freeboard.comment.domain.Comment;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponse {

	private final Long id;
	private final String author;
	private final String content;

	@Builder
	public CommentResponse(Long id, String author, String content) {
		this.id = id;
		this.author = author;
		this.content = content;
	}

	public static CommentResponse of(Comment comment) {
		return CommentResponse.builder()
			.id(comment.getId())
			.author(comment.getAuthor())
			.content(comment.getContent())
			.build();
	}
}
