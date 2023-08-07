package com.freeboard.exception;

public class NotFoundException extends RootException {

	public NotFoundException() {
		super(ErrorCode.NOT_FOUND);
	}
}
