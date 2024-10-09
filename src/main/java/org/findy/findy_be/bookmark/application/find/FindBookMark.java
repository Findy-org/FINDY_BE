package org.findy.findy_be.bookmark.application.find;

import org.findy.findy_be.bookmark.domain.Bookmark;

public interface FindBookMark {

	Bookmark invokeById(Long bookmark_id);
}
