package org.findy.findy_be.bookmark.application.delete;

import static org.findy.findy_be.common.exception.ErrorCode.*;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteBookmarkService implements DeleteBookmark {

	private final BookmarkRepository bookmarkRepository;

	public void invoke(String userId, Long bookmarkId) {

		Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
			.orElseThrow(
				() -> new EntityNotFoundException(String.format(NOT_FOUND_BOOKMARK_BY_ID.getMessage(), bookmarkId)));
		validateBookmarkUser(userId, bookmark);
		bookmarkRepository.delete(bookmark);
	}

	private static void validateBookmarkUser(final String userId, final Bookmark bookmark) {
		if (!bookmark.getUser().getUserId().equals(userId) || bookmark.getName().equals("내 장소")) {
			throw new IllegalArgumentException(FORBIDDEN_BOOKMARK_ACCESS.getMessage());
		}
	}
}
