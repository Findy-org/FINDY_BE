package org.findy.findy_be.bookmark.application.register;

import org.findy.findy_be.bookmark.dto.request.YoutubeBookmarkRequest;

public interface RegisterYoutubeBookmark {
	void invoke(final String userId, final YoutubeBookmarkRequest request);
}
