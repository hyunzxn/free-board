package com.freeboard.comment.service.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateServiceRequest {

	private String password;
	private String content;

	@Builder
	public CommentCreateServiceRequest(String password, String content) {
		this.password = password;
		this.content = content;
	}

}
