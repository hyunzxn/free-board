package com.freeboard.service.comment;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freeboard.domain.comment.Comment;
import com.freeboard.domain.post.Post;
import com.freeboard.domain.user.User;
import com.freeboard.exception.NotFoundException;
import com.freeboard.repository.comment.CommentRepository;
import com.freeboard.repository.post.PostRepository;
import com.freeboard.repository.user.UserRepository;
import com.freeboard.request.comment.CommentCreateServiceRequest;
import com.freeboard.response.comment.CommentResponse;

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
	public CommentResponse createComment(Long userId, Long postId, CommentCreateServiceRequest request) {
		User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
		Post post = postRepository.findById(postId).orElseThrow(NotFoundException::new);

		String encryptedPassword = passwordEncoder.encode(request.getPassword());

		Comment comment = Comment.of(encryptedPassword, request.getContent());
		comment.setAuthor(user.getNickname());
		comment.setPost(post);
		Comment savedComment = commentRepository.save(comment);
		return CommentResponse.of(savedComment);
	}
}
