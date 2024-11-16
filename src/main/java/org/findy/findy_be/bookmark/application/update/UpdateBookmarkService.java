package org.findy.findy_be.bookmark.application.update;

import static org.findy.findy_be.common.exception.ErrorCode.*;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.dto.request.UpdateBookmarkRequest;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateBookmarkService implements UpdateBookmark {

	private final BookmarkRepository bookmarkRepository;

	@Override
	public void invoke(final String userId, final Long bookmarkId, final UpdateBookmarkRequest request) {
		Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
			.orElseThrow(
				() -> new EntityNotFoundException(String.format(NOT_FOUND_BOOKMARK_BY_ID.getMessage(), bookmarkId)));
		validateBookmarkUser(userId, bookmark);
		bookmark.updateName(request.name());
	}

	private static void validateBookmarkUser(final String userId, final Bookmark bookmark) {
		if (!bookmark.getUser().getUserId().equals(userId)) {
			throw new IllegalArgumentException(FORBIDDEN_BOOKMARK_ACCESS.getMessage());
		}
	}
}
