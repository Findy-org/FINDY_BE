package org.findy.findy_be.marker.application.register;

import static org.findy.findy_be.common.exception.ErrorCode.*;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.domain.BookmarkType;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.marker.application.create.CreateMarker;
import org.findy.findy_be.marker.dto.request.RegisterSearchedMarkerRequest;
import org.findy.findy_be.place.application.find.FindPlace;
import org.findy.findy_be.marker.application.domain.Place;
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
public class RegisterMarkerService implements RegisterMarker {

	private final FindPlace findPlace;
	private final BookmarkRepository bookmarkRepository;
	private final CreateMarker createMarker;
	private final PlaceRepository placeRepository;

	@Override
	public void invoke(final Long bookmarkId, final RegisterSearchedMarkerRequest request, final String userId) {
		Bookmark bookmark = bookmarkRepository.findByIdAndUserUserId(bookmarkId, userId)
			.orElseThrow(
				() -> new EntityNotFoundException(String.format(NOT_FOUND_BOOKMARK_BY_ID.getMessage(), bookmarkId)));

		validateBookmarkType(bookmark);

		findPlace.invoke(request.title(), request.roadAddress()).ifPresentOrElse(
			existingPlace -> {
				createMarker.invoke(bookmark, existingPlace);
			},
			() -> {
				log.info("저장된 장소가 없어 새로운 장소를 저장합니다.");
				Place newPlace = placeRepository.save(request.toEntity());
				createMarker.invoke(bookmark, newPlace);
			}
		);
	}

	private void validateBookmarkType(Bookmark bookmark) {
		if (bookmark.getBookmarkType().equals(BookmarkType.YOUTUBE)) {
			throw new IllegalArgumentException(BAD_REQUEST_YOUTUBE_BOOKMARK_REGISTER_ERROR.getMessage());
		}
	}
}

