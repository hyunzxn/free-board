package com.freeboard.common.exception;

public class AlreadyExistException extends RootException {

	public AlreadyExistException(String message) {
		super(message, ErrorCode.BAD_REQUEST);
	}
}
