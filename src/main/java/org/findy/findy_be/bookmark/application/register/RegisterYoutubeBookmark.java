package org.findy.findy_be.bookmark.application.register;

import org.findy.findy_be.bookmark.dto.request.RegisterYoutubeBookmarkRequest;
import org.findy.findy_be.user.domain.User;

public interface RegisterYoutubeBookmark {

	void invoke(final User user, final RegisterYoutubeBookmarkRequest request);
}
