package com.freeboard.comment.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freeboard.comment.controller.response.CommentResponse;
import com.freeboard.comment.domain.Comment;
import com.freeboard.comment.repository.CommentRepository;
import com.freeboard.comment.service.request.CommentCreateServiceRequest;
import com.freeboard.common.exception.NotFoundException;
import com.freeboard.post.domain.Post;
import com.freeboard.post.repository.PostRepository;
import com.freeboard.user.domain.User;
import com.freeboard.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public CommentResponse create(Long userId, Long postId, CommentCreateServiceRequest request) {
		User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
		Post post = postRepository.findById(postId).orElseThrow(NotFoundException::new);

		String encryptedPassword = passwordEncoder.encode(request.getPassword());

		Comment comment = Comment.of(encryptedPassword, request.getContent());
		comment.setAuthor(user.getNickname());
		comment.setPost(post);
		Comment savedComment = commentRepository.save(comment);
		return CommentResponse.of(savedComment);
	}

	@Transactional
	public void delete(Long id) {
		Comment comment = commentRepository.findById(id).orElseThrow(NotFoundException::new);
		commentRepository.delete(comment);
	}
}
