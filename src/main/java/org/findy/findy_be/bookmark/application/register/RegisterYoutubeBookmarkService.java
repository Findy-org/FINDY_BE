package org.findy.findy_be.bookmark.application.register;

import static org.findy.findy_be.common.exception.ErrorCode.*;

import java.util.List;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.dto.request.YoutubeBookmarkRequest;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.common.exception.custom.ForbiddenAccessException;
import org.findy.findy_be.place.application.register.BatchRegisterPlace;
import org.findy.findy_be.place.dto.request.RegisterPlaceRequest;
import org.findy.findy_be.user.application.UserService;
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

	private final UserService userService;
	private final BookmarkRepository bookmarkRepository;
	private final BatchRegisterPlace batchRegisterPlace;

	@Override
	public void invoke(final String userId, final YoutubeBookmarkRequest request) {
		User user = userService.findUser(userId);
		List<RegisterPlaceRequest> placeRequests = request.places();
		bookmarkRepository.findByUserAndYoutuberId(user, request.youtuberId()).ifPresentOrElse(
			existingBookmark -> {
				validateBookmarkOwner(userId, existingBookmark);
				batchRegisterPlace.invoke(existingBookmark, placeRequests);
			},
			() -> {
				log.info("새로운 북마크에 저장합니다.");
				Bookmark bookmark = Bookmark.createYoutubeType(request.youtuberName(), request.youtuberId(),
					request.youtuberProfile(), user);
				bookmarkRepository.save(bookmark);
				batchRegisterPlace.invoke(bookmark, placeRequests);
			}
		);
	}

	private void validateBookmarkOwner(String userId, Bookmark bookmark) {
		String bookmarkUserId = bookmark.getUser().getUserId();
		if (!bookmarkUserId.equals(userId)) {
			throw new ForbiddenAccessException(FORBIDDEN_BOOKMARK_ACCESS.getMessage());
		}
	}
}
