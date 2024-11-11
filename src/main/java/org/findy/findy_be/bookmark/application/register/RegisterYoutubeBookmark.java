package org.findy.findy_be.bookmark.application.register;

import org.findy.findy_be.bookmark.dto.request.YoutubeBookmarkRequest;
import org.findy.findy_be.user.domain.User;

public interface RegisterYoutubeBookmark {

	void invoke(final User user, final YoutubeBookmarkRequest request);
}
