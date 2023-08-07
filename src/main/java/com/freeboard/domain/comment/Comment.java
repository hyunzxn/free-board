package com.freeboard.domain.comment;

import com.freeboard.domain.post.Post;
import com.freeboard.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
	indexes = {
		@Index(name = "IDX_COMMENT_POST_ID", columnList = "post_id")
	}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String author;

	@Column(nullable = false)
	private String password;

	@Lob
	@Column(nullable = false)
	private String content;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;

	@Builder
	public Comment(String content, String password) {
		this.content = content;
		this.password = password;
	}

	public static Comment of(String password, String content) {
		return Comment.builder()
			.password(password)
			.content(content)
			.build();
	}

	public void setAuthor(String nickName) {
		this.author = nickName;
	}

	public void setPost(Post post) {
		this.post = post;
	}
}
