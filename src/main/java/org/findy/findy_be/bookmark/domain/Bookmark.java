package org.findy.findy_be.bookmark.domain;

import org.findy.findy_be.common.entity.BaseEntity;
import org.findy.findy_be.user.domain.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "bookmarks")
public class Bookmark extends BaseEntity {

	private String name;

	@Enumerated(EnumType.STRING)
	@NotNull
	private BookmarkType bookmarkType;

	private String youtuberId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	public static Bookmark of(String name, BookmarkType type, String youtuberId, User user) {
		return Bookmark.builder()
			.name(name)
			.bookmarkType(type)
			.youtuberId(youtuberId)
			.user(user)
			.build();
	}

	public static Bookmark createYoutubeType(String name, String youtuberId, User user) {
		return Bookmark.of(name, BookmarkType.YOUTUBE, youtuberId, user);
	}

	public static Bookmark createCustomType(String name, User user) {
		return Bookmark.of(name, BookmarkType.CUSTOM, null, user);
	}
}
