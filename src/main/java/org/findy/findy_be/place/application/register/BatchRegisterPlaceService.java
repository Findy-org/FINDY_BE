package org.findy.findy_be.place.application.register;

import java.util.List;
import java.util.stream.Collectors;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.marker.application.create.BatchCreateMarker;
import org.findy.findy_be.place.application.find.FindPlace;
import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.dto.request.RegisterPlaceRequest;
import org.findy.findy_be.place.repository.PlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BatchRegisterPlaceService implements BatchRegisterPlace {

	private final FindPlace findPlace;
	private final PlaceRepository placeRepository;
	private final BatchCreateMarker batchCreateMarker;

	@Override
	public void invoke(final Bookmark bookmark, final List<RegisterPlaceRequest> requests) {
		List<Place> places = requests.stream()
			.filter(request -> findPlace.invoke(request.title(), request.roadAddress()).isEmpty())
			.map(RegisterPlaceRequest::toEntity)
			.collect(Collectors.toList());
		List<Place> persistedPlaces = placeRepository.bulkInsert(places);
		batchCreateMarker.invoke(bookmark, persistedPlaces);
	}
}
