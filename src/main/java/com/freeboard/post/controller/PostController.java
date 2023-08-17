package com.freeboard.post.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freeboard.common.response.ApiResponse;
import com.freeboard.post.controller.request.PostCreateRequest;
import com.freeboard.post.controller.request.PostSearch;
import com.freeboard.post.controller.request.PostUpdateRequest;
import com.freeboard.post.controller.response.PostResponse;
import com.freeboard.post.service.PostService;
import com.freeboard.user.domain.UserPrincipal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

	private final PostService postService;

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/posts")
	public ApiResponse<PostResponse> create(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestBody @Valid PostCreateRequest request) {
		PostResponse postResponse = postService.createPost(userPrincipal.getUserId(), request.toServiceRequest());
		return ApiResponse.of(HttpStatus.CREATED, "새로운 게시글 생성 성공", postResponse);
	}

	@GetMapping("/posts")
	public ApiResponse<List<PostResponse>> getAll(@ModelAttribute PostSearch postSearch) {
		List<PostResponse> posts = postService.getAllPost(postSearch);
		return ApiResponse.ok(posts);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/posts/{id}")
	public ApiResponse<PostResponse> getOne(@PathVariable Long id) {
		PostResponse postResponse = postService.getOnePost(id);
		return ApiResponse.ok(postResponse);
	}

	@PreAuthorize("isAuthenticated() && hasPermission(#id, 'Post', 'PUT')")
	@PutMapping("/posts/{id}")
	public ApiResponse<PostResponse> update(
		@PathVariable Long id,
		@RequestBody PostUpdateRequest request) {
		PostResponse postResponse = postService.updatePost(id, request);
		return ApiResponse.ok(postResponse);
	}

	@PreAuthorize("isAuthenticated() && hasPermission(#id, 'Post', 'DELETE')")
	@DeleteMapping("/posts/{id}")
	public void delete(@PathVariable Long id) {
		postService.deletePost(id);
	}

}
