package com.freeboard.common.response;

import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {

	private final int code;
	private final String message;
	private final Map<String, String> invalidatedValue;

	@Builder
	public ErrorResponse(int code, String message, Map<String, String> invalidatedValue) {
		this.code = code;
		this.message = message;
		this.invalidatedValue = invalidatedValue != null ? invalidatedValue : new HashMap<>();
	}

	public void addValidation(String fieldName, String errorMessage) {
		this.invalidatedValue.put(fieldName, errorMessage);
	}
}
