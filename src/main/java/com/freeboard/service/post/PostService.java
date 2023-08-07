package com.freeboard.service.post;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freeboard.domain.post.Post;
import com.freeboard.domain.user.User;
import com.freeboard.exception.NotFoundException;
import com.freeboard.repository.post.PostRepository;
import com.freeboard.repository.user.UserRepository;
import com.freeboard.request.post.PostCreateServiceRequest;
import com.freeboard.request.post.PostSearch;
import com.freeboard.request.post.PostUpdateRequest;
import com.freeboard.response.post.PostResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;

	@Transactional
	public PostResponse createPost(Long userId, PostCreateServiceRequest request) {
		User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);

		Post post = Post.of(request.getPostType(), request.getTitle(), request.getContent());
		post.setUser(user);
		Post savedPost = postRepository.save(post);
		return PostResponse.of(savedPost);
	}

	public List<PostResponse> getAllPost(PostSearch postSearch) {
		return postRepository.getAll(postSearch)
			.stream()
			.map(PostResponse::of)
			.collect(Collectors.toList());
	}

	public PostResponse getOnePost(Long id) {
		Post post = postRepository.findById(id).orElseThrow(NotFoundException::new);
		return PostResponse.of(post);
	}

	@Transactional
	public PostResponse updatePost(Long id, PostUpdateRequest request) {
		Post post = postRepository.findById(id).orElseThrow(NotFoundException::new);
		post.update(request);
		return PostResponse.of(post);
	}

	@Transactional
	public void deletePost(Long id) {
		Post post = postRepository.findById(id).orElseThrow(NotFoundException::new);
		postRepository.deleteById(post.getId());
	}
}
