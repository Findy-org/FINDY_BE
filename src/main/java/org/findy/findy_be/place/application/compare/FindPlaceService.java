package org.findy.findy_be.place.application.compare;

import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.dto.PlaceRequest;
import org.findy.findy_be.place.repository.PlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindPlaceService implements FindPlace {

	private final PlaceRepository placeRepository;

	public Place invoke(PlaceRequest request) {
		return placeRepository.findPlaceByDetails(
			request.title(),
			request.address(),
			request.roadAddress(),
			request.mapx(),
			request.mapy(),
			request.majorCategory()
		);
	}
}