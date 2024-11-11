package org.findy.findy_be.bookmark.application.create;

import org.findy.findy_be.bookmark.dto.request.CreateCustomBookmarkRequest;
import org.findy.findy_be.user.domain.User;

public interface CreateCustomBookmark {

	void invoke(final User user, final CreateCustomBookmarkRequest request);
}
