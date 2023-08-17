package com.freeboard.config;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import com.freeboard.common.exception.NotFoundException;
import com.freeboard.post.domain.Post;
import com.freeboard.post.repository.PostRepository;
import com.freeboard.user.domain.UserPrincipal;

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

		UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();

		Post post = postRepository.findById((Long)targetId)
			.orElseThrow(NotFoundException::new);

		if (!post.getUserId().equals(userPrincipal.getUserId())) {
			log.error("[인가실패] 해당 사용자가 작성한 글이 아닙니다. postId={}", targetId);
			return false;
		}

		return true;
	}
}
