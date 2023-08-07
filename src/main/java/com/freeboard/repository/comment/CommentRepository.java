package com.freeboard.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.freeboard.domain.comment.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
