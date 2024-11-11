package org.findy.findy_be.bookmark.application.create;

import static org.findy.findy_be.common.exception.ErrorCode.*;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.dto.request.CreateCustomBookmarkRequest;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.user.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CreateCustomBookmarkService implements CreateCustomBookmark {

	private final BookmarkRepository bookmarkRepository;

	@Override
	public void invoke(final User user, final CreateCustomBookmarkRequest request) {
		bookmarkRepository.findByName(request.name()).ifPresent(existingBookmark -> {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
				String.format(BAD_REQUEST_BOOKMARK_NAME.getMessage(), request.name()));
		});
		Bookmark bookmark = request.toEntity(user);
		bookmarkRepository.save(bookmark);
	}
}
