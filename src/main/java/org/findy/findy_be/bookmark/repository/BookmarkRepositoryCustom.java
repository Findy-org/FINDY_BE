package org.findy.findy_be.bookmark.repository;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BookmarkRepositoryCustom {
	Slice<Bookmark> findBookmarksByUser(User user, Pageable pageable, Long cursor);
}
