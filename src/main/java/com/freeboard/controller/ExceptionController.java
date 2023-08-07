package com.freeboard.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.freeboard.exception.RootException;
import com.freeboard.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

	@ExceptionHandler(RootException.class)
	public ResponseEntity<ErrorResponse> rootExceptionHandler(RootException e) {
		log.error(e.getErrorCode().getMessage(), e);
		ErrorResponse response = ErrorResponse.builder()
			.code(e.getErrorCode().getStatusCode())
			.message(e.getMessage())
			.build();

		return ResponseEntity
			.status(e.getErrorCode().getStatusCode())
			.body(response);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorResponse validationExceptionHandler(MethodArgumentNotValidException e) {
		log.error(e.getMessage(), e);
		ErrorResponse errorResponse = ErrorResponse.builder()
			.code(400)
			.message("Validation Fail")
			.build();

		for (FieldError fieldError : e.getFieldErrors()) {
			errorResponse.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
		}

		return errorResponse;
	}
}
