package org.findy.findy_be.bookmark.dto.response;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.domain.BookmarkType;

import lombok.Builder;

@Builder
public record BookmarkResponse(
	Long bookmarkId,
	String youtuberProfile,
	String name,
	Long markersCount,
	BookmarkType bookmarkType,
	String youtubeLink
) {
	public static BookmarkResponse from(final Bookmark bookmark) {
		return BookmarkResponse.builder()
			.bookmarkId(bookmark.getId())
			.youtuberProfile(bookmark.getYoutuberProfile())
			.name(bookmark.getName())
			.markersCount(bookmark.getMarkersCount())
			.bookmarkType(bookmark.getBookmarkType())
			.youtubeLink(bookmark.getYoutubeLink())
			.build();
	}
}
