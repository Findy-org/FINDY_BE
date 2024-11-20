package org.findy.findy_be.place.application.find;

import java.util.Optional;

import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.domain.vo.Category;
import org.findy.findy_be.place.repository.PlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindPlaceService implements FindPlace {

	private final PlaceRepository placeRepository;

	public Optional<Place> invoke(final String title, final String roadAddress, final Category category) {
		return placeRepository.findPlaceByDetails(title, roadAddress, category.getMajorCategory(),
			category.getMiddleCategory());
	}
}