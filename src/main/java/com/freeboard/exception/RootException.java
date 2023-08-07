package com.freeboard.exception;

import lombok.Getter;

@Getter
public abstract class RootException extends RuntimeException {

	private final ErrorCode errorCode;

	public RootException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public RootException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

}
