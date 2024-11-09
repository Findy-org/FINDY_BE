package org.findy.findy_be.bookmark.repository;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BookmarkRepositoryCustom {
	Slice<Bookmark> findBookmarksByUserId(String userId, Pageable pageable, Long cursor);
}
