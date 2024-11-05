package org.findy.findy_be.place.application.register;

import static org.findy.findy_be.common.exception.ErrorCode.*;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.domain.BookmarkType;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.common.exception.custom.ForbiddenAccessException;
import org.findy.findy_be.marker.application.create.CreateMarker;
import org.findy.findy_be.place.application.find.FindPlace;
import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.dto.request.RegisterPlaceRequest;
import org.findy.findy_be.place.repository.PlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RegisterPlaceService implements RegisterPlace {

	private final FindPlace findPlace;
	private final BookmarkRepository bookmarkRepository;
	private final CreateMarker createMarker;
	private final PlaceRepository placeRepository;

	@Override
	public void invoke(final String userId, final RegisterPlaceRequest request, final Long bookmarkId) {
		Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
			.orElseThrow(
				() -> new EntityNotFoundException(String.format(NOT_FOUND_BOOKMARK_BY_ID.getMessage(), bookmarkId)));

		validateBookmarkOwner(userId, bookmark);
		validateBookmarkType(bookmark);

		findPlace.invoke(request.title(), request.roadAddress()).ifPresentOrElse(
			existingPlace -> {
				createMarker.invoke(bookmark, existingPlace);
			},
			() -> {
				log.info("저장된 장소가 없어 새로운 장소를 저장합니다.");
				Place newPlace = createAndSaveNewPlace(request);
				createMarker.invoke(bookmark, newPlace);
			}
		);
		bookmark.incrementMarkersCount(1);
	}

	private void validateBookmarkOwner(String userId, Bookmark bookmark) {
		String bookmarkUserId = bookmark.getUser().getUserId();
		if (!bookmarkUserId.equals(userId)) {
			throw new ForbiddenAccessException(FORBIDDEN_BOOKMARK_ACCESS.getMessage());
		}
	}

	private void validateBookmarkType(Bookmark bookmark) {
		if (bookmark.getBookmarkType().equals(BookmarkType.YOUTUBE)) {
			throw new IllegalArgumentException(BAD_REQUEST_YOUTUBE_BOOKMARK_REGISTER_ERROR.getMessage());
		}
	}

	private Place createAndSaveNewPlace(RegisterPlaceRequest request) {
		Place newPlace = Place.create(request);
		return placeRepository.save(newPlace);
	}
}

