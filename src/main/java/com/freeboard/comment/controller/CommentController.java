package com.freeboard.comment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freeboard.comment.controller.request.CommentCreateRequest;
import com.freeboard.comment.controller.response.CommentResponse;
import com.freeboard.comment.service.CommentService;
import com.freeboard.common.response.ApiResponse;
import com.freeboard.user.domain.UserPrincipal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class CommentController {

	private final CommentService commentService;

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/posts/{postId}/comments")
	public ApiResponse<CommentResponse> createComment(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable Long postId,
		@RequestBody @Valid CommentCreateRequest request) {
		CommentResponse response = commentService.createComment(userPrincipal.getUserId(), postId,
			request.toServiceRequest());
		return ApiResponse.of(HttpStatus.CREATED, "댓글 생성", response);
	}
}
