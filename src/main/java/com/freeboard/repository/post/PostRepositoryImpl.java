package com.freeboard.repository.post;

import static com.freeboard.domain.post.QPost.*;

import java.util.List;

import com.freeboard.domain.post.Post;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.freeboard.request.post.PostSearch;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Post> getAll(PostSearch postSearch) {
		return jpaQueryFactory
			.selectFrom(post)
			.limit(postSearch.getSize())
			.offset(postSearch.getOffset())
			.orderBy(sortDirection(postSearch.getSort(), post.id))
			.fetch();
	}

	private OrderSpecifier<?> sortDirection(String sort, NumberPath<Long> id) {
		Order direction = sort.equalsIgnoreCase("DESC") ? Order.DESC : Order.ASC;
		return new OrderSpecifier<>(direction, id);
	}
}
