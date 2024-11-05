package org.findy.findy_be.bookmark.application.find;

import org.findy.findy_be.bookmark.dto.response.BookmarkResponse;

public interface FindBookMark {
	BookmarkResponse invoke(Long bookmarkId);
}
