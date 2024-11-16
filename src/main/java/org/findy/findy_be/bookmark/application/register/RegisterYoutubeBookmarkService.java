package org.findy.findy_be.bookmark.application.register;

import static org.findy.findy_be.common.exception.ErrorCode.*;

import java.util.List;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.dto.request.YoutubeBookmarkRequest;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.common.exception.custom.ForbiddenAccessException;
import org.findy.findy_be.place.application.register.BatchRegisterPlace;
import org.findy.findy_be.place.dto.request.RegisterPlaceRequest;
import org.findy.findy_be.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RegisterYoutubeBookmarkService implements RegisterYoutubeBookmark {

	private final BookmarkRepository bookmarkRepository;
	private final BatchRegisterPlace batchRegisterPlace;

	@Override
	public void invoke(final User user, final YoutubeBookmarkRequest request) {
		List<RegisterPlaceRequest> placeRequests = request.places();
		bookmarkRepository.findByUserAndYoutuberId(user, request.youtuberId()).ifPresentOrElse(
			existingBookmark -> {
				existingBookmark.updateName(request.youtuberName());
				validateBookmarkOwner(user, existingBookmark.getUser());
				batchRegisterPlace.invoke(existingBookmark, placeRequests);
			},
			() -> {
				log.info("새로운 북마크에 저장합니다.");
				Bookmark bookmark = request.toEntity(user);
				bookmarkRepository.save(bookmark);
				batchRegisterPlace.invoke(bookmark, placeRequests);
			}
		);
	}

	private void validateBookmarkOwner(User currentUser, User bookmarkUser) {
		String bookmarkUserId = bookmarkUser.getUserId();
		if (!bookmarkUserId.equals(currentUser.getUserId())) {
			throw new ForbiddenAccessException(FORBIDDEN_BOOKMARK_ACCESS.getMessage());
		}
	}
}
