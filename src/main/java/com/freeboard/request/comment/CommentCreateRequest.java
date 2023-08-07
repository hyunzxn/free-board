package com.freeboard.request.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateRequest {

	@NotBlank(message = "비밀번호를 입력해주세요.")
	private String password;

	@NotBlank(message = "댓글 내용을 작성해주세요.")
	private String content;

	@Builder
	public CommentCreateRequest(String password, String content) {
		this.password = password;
		this.content = content;
	}

	public CommentCreateServiceRequest toServiceRequest() {
		return CommentCreateServiceRequest.builder()
			.password(password)
			.content(content)
			.build();
	}

}
