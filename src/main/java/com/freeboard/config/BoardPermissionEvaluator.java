package com.freeboard.config;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import com.freeboard.domain.post.Post;
import com.freeboard.domain.user.UserPrincipal;
import com.freeboard.exception.NotFoundException;
import com.freeboard.repository.post.PostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class BoardPermissionEvaluator implements PermissionEvaluator {

	private final PostRepository postRepository;

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
		Object permission) {

		UserPrincipal userPrincipal  = (UserPrincipal) authentication.getPrincipal();

		Post post = postRepository.findById((Long)targetId)
			.orElseThrow(NotFoundException::new);

		if (!post.getUserId().equals(userPrincipal.getUserId())) {
			log.error("[인가실패] 해당 사용자가 작성한 글이 아닙니다. postId={}", targetId);
			return false;
		}

		return true;
	}
}