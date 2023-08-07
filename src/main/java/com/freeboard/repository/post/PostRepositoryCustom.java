package com.freeboard.repository.post;

import java.util.List;

import com.freeboard.domain.post.Post;
import com.freeboard.request.post.PostSearch;

public interface PostRepositoryCustom {

	List<Post> getAll(PostSearch postSearch);
}
