package com.freeboard.exception;

public class AlreadyExistException extends RootException {

	public AlreadyExistException(String message) {
		super(message, ErrorCode.BAD_REQUEST);
	}
}
