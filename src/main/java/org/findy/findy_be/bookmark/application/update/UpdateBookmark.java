package org.findy.findy_be.bookmark.application.update;

import org.findy.findy_be.bookmark.dto.request.UpdateBookmarkRequest;

public interface UpdateBookmark {

	void invoke(String userId, Long bookmarkId, UpdateBookmarkRequest request);
}
