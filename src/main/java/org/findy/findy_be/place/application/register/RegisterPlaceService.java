package org.findy.findy_be.place.application.register;

import static org.findy.findy_be.common.exception.ErrorMessage.*;

import org.findy.findy_be.bookmark.application.find.FindBookMark;
import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.domain.BookmarkType;
import org.findy.findy_be.marker.application.create.CreateMarker;
import org.findy.findy_be.place.application.find.FindPlace;
import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.dto.request.PlaceRequest;
import org.findy.findy_be.place.repository.PlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RegisterPlaceService implements RegisterPlace {

	private final FindPlace findPlace;
	private final FindBookMark findBookMark;
	private final CreateMarker createMarker;
	private final PlaceRepository placeRepository;

	@Override
	public void invoke(PlaceRequest request) {
		Bookmark bookmark = findBookMark.invokeById(request.bookmarkId());

		validateBookmarkType(bookmark);

		findPlace.invoke(request).ifPresentOrElse(
			existingPlace -> createMarker.invoke(bookmark, existingPlace),
			() -> {
				log.info("저장된 장소가 없어 새로운 장소를 저장합니다.");
				Place newPlace = createAndSaveNewPlace(request);
				createMarker.invoke(bookmark, newPlace);
			}
		);
	}

	private void validateBookmarkType(Bookmark bookmark) {
		if (bookmark.getBookmarkType().equals(BookmarkType.YOUTUBE)) {
			throw new IllegalArgumentException(YOUTUBE_BOOKMARK_REGISTER_ERROR.getMessage());
		}
	}

	private Place createAndSaveNewPlace(PlaceRequest request) {
		Place newPlace = Place.create(request);
		return placeRepository.save(newPlace);
	}
}
