package org.findy.findy_be.bookmark.application.register;

import static org.findy.findy_be.common.exception.ErrorCode.*;

import java.util.List;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.dto.request.RegisterYoutubeBookmarkRequest;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.common.exception.custom.ForbiddenAccessException;
import org.findy.findy_be.marker.application.register.BatchRegisterMarker;
import org.findy.findy_be.marker.dto.request.RegisterMarkerRequest;
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
	private final BatchRegisterMarker<RegisterMarkerRequest> batchRegisterMarker;

	@Override
	public void invoke(final User user, final RegisterYoutubeBookmarkRequest request) {
		List<RegisterMarkerRequest> placeRequests = request.places();

		bookmarkRepository.findByUserAndYoutuberId(user, request.youtuberId())
			.ifPresentOrElse(
				existingBookmark -> {
					existingBookmark.updateName(request.youtuberName());
					validateBookmarkOwner(user, existingBookmark.getUser());
					batchRegisterMarker.invoke(existingBookmark, placeRequests);
				},
				() -> {
					log.info("새로운 북마크에 저장합니다.");
					Bookmark newBookmark = bookmarkRepository.save(request.toEntity(user));
					batchRegisterMarker.invoke(newBookmark, placeRequests);
				}
			);
	}

	private void validateBookmarkOwner(User currentUser, User bookmarkUser) {
		if (!bookmarkUser.getUserId().equals(currentUser.getUserId())) {
			throw new ForbiddenAccessException(FORBIDDEN_BOOKMARK_ACCESS.getMessage());
		}
	}
}
