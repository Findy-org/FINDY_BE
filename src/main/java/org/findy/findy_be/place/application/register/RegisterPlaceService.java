package org.findy.findy_be.place.application.register;

import org.findy.findy_be.bookmark.application.find.FindBookMark;
import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.marker.application.create.CreateMarker;
import org.findy.findy_be.place.application.compare.FindPlace;
import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.dto.PlaceRequest;
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
		Place place = findPlace.invoke(request);
		if (place == null) {
			log.info("저장된 장소가 없어 새로운 장소를 저장합니다.");
			place = createAndSaveNewPlace(request);
		}
		createMarker.invoke(bookmark, place);
	}

	private Place createAndSaveNewPlace(PlaceRequest request) {
		Place newPlace = Place.create(
			request.address(),
			request.description(),
			request.link(),
			request.majorCategory(),
			request.mapx(),
			request.mapy(),
			request.middleCategory(),
			request.roadAddress(),
			request.telephone(),
			request.title()
		);
		return placeRepository.save(newPlace);
	}
}