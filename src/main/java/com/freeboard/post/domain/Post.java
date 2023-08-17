package com.freeboard.post.domain;

import java.util.ArrayList;
import java.util.List;

import com.freeboard.comment.domain.Comment;
import com.freeboard.common.domain.BaseEntity;
import com.freeboard.post.controller.request.PostUpdateRequest;
import com.freeboard.user.domain.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Post extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private PostType postType;

	@NotBlank
	private String title;

	@NotBlank
	@Lob
	private String content;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	private List<Comment> comments = new ArrayList<>();

	@Builder
	private Post(PostType postType, String title, String content, User user) {
		this.postType = postType;
		this.title = title;
		this.content = content;
		this.user = user;
	}

	public static Post of(PostType postType, String title, String content) {
		return Post.builder()
			.postType(postType)
			.title(title)
			.content(content)
			.build();
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void update(PostUpdateRequest request) {
		this.postType = request.getPostType();
		this.title = request.getTitle();
		this.content = request.getContent();
	}

	public Long getUserId() {
		return this.user.getId();
	}
}
