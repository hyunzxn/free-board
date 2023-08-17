package com.freeboard.common.exception;

public class NotFoundException extends RootException {

	public NotFoundException() {
		super(ErrorCode.NOT_FOUND);
	}
}
