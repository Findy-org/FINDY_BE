package org.findy.findy_be.bookmark.application.find;

import org.findy.findy_be.bookmark.dto.response.BookmarkResponse;
import org.findy.findy_be.common.dto.pagination.response.SliceResponse;

public interface FindAllPagedBookmarks {
	SliceResponse<BookmarkResponse> invoke(final String userId, final Long cursor, final int size);
}
