package org.findy.findy_be.bookmark.repository;

import java.util.List;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.domain.QBookmark;
import org.findy.findy_be.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BookmarkRepositoryCustomImpl implements BookmarkRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<Bookmark> findBookmarksByUser(User user, Pageable pageable, Long cursor) {
		QBookmark bookmark = QBookmark.bookmark;

		List<Bookmark> bookmarks = queryFactory
			.selectFrom(bookmark)
			.where(bookmark.user.eq(user)
				.and(cursor != null ? bookmark.id.gt(cursor) : null))
			.orderBy(bookmark.id.asc())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		boolean hasNext = bookmarks.size() > pageable.getPageSize();
		if (hasNext) {
			bookmarks = bookmarks.subList(0, pageable.getPageSize());
		}

		return new SliceImpl<>(bookmarks, pageable, hasNext);
	}
}
