package org.findy.findy_be.bookmark.application.find;

import java.util.List;

import org.findy.findy_be.bookmark.dto.response.BookmarkResponse;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.common.dto.pagination.response.SliceResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindAllPagedBookmarksService implements FindAllPagedBookmarks {

	private final BookmarkRepository bookmarkRepository;

	@Override
	public SliceResponse<BookmarkResponse> invoke(final String userId, final Long cursor, final int size) {
		Pageable pageable = PageRequest.of(0, size);
		var bookmarkSlice = bookmarkRepository.findBookmarksByUserId(userId, pageable, cursor);

		List<BookmarkResponse> data = bookmarkSlice.stream()
			.map(BookmarkResponse::from)
			.toList();
		Long nextCursor = bookmarkSlice.hasNext() ? data.get(data.size() - 1).bookmarkId() : null;

		return new SliceResponse<>(data, cursor != null ? cursor.intValue() : 0, size, bookmarkSlice.hasNext(),
			nextCursor);
	}
}
