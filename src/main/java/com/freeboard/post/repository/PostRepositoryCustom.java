package com.freeboard.post.repository;

import java.util.List;

import com.freeboard.post.controller.request.PostSearch;
import com.freeboard.post.domain.Post;

public interface PostRepositoryCustom {

	List<Post> getAll(PostSearch postSearch);
}
