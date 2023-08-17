package com.freeboard.common.util;

import static java.lang.Math.*;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCond {

	private static final int MAX_SIZE = 2000;

	@Builder.Default
	private Integer page = 1; // 페이지 번호

	@Builder.Default
	private Integer size = 10; // 페이지 사이즈

	@Builder.Default
	private String sort = "DESC"; // 정렬 기준: 내림차순 디폴트

	public long getOffset() {
		return (long)(max(1, page) - 1) * min(size, MAX_SIZE);
	}
}
