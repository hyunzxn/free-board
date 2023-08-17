package com.freeboard.post.controller.request;

import com.freeboard.common.util.SearchCond;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSearch extends SearchCond {

	private String title;

	private String content;

	@Builder
	public PostSearch(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
