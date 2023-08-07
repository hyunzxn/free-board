package com.freeboard.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.freeboard.domain.post.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom{

}
