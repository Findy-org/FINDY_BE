package org.findy.findy_be.bookmark.application.register;

import org.findy.findy_be.bookmark.dto.request.RegisterNaverBookmarkRequest;
import org.findy.findy_be.user.domain.User;

public interface RegisterNaverBookmark {

	void invoke(final User user, final RegisterNaverBookmarkRequest request);
}
