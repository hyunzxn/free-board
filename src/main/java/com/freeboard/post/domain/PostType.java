package com.freeboard.post.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PostType {

	FREE("자유"),
	ASK("질문"),
	INFORMATION("정보 공유");

	private final String text;
}
