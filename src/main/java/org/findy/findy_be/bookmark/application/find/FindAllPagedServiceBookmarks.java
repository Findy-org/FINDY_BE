package org.findy.findy_be.bookmark.application.find;

import java.util.List;

import org.findy.findy_be.bookmark.dto.response.BookmarkResponse;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.common.dto.pagination.SliceResponse;
import org.findy.findy_be.user.application.UserService;
import org.findy.findy_be.user.domain.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindAllPagedServiceBookmarks implements FindAllPagedBookmarks {

	private final UserService userService;
	private final BookmarkRepository bookmarkRepository;

	@Override
	public SliceResponse<BookmarkResponse> invoke(final String userId, final Long cursor, final int size) {
		User user = userService.findUser(userId);
		Pageable pageable = PageRequest.of(0, size);
		var bookmarkSlice = bookmarkRepository.findBookmarksByUser(user, pageable, cursor);

		List<BookmarkResponse> data = bookmarkSlice.stream()
			.map(BookmarkResponse::from)
			.toList();
		Long nextCursor = bookmarkSlice.hasNext() ? data.get(data.size() - 1).bookmarkId() : null;

		return new SliceResponse<>(data, cursor != null ? cursor.intValue() : 0, size, bookmarkSlice.hasNext(),
			nextCursor);
	}
}
