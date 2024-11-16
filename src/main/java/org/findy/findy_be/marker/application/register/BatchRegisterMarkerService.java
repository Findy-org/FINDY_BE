package org.findy.findy_be.marker.application.register;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.marker.application.create.BatchCreateMarker;
import org.findy.findy_be.place.application.find.FindPlace;
import org.findy.findy_be.marker.application.domain.Place;
import org.findy.findy_be.marker.dto.request.RegisterMarkerRequest;
import org.findy.findy_be.place.repository.PlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BatchRegisterMarkerService implements BatchRegisterMarker {

	private static final String KEY_DELIMITER = "::";

	private final FindPlace findPlace;
	private final PlaceRepository placeRepository;
	private final BatchCreateMarker batchCreateMarker;

	@Override
	public void invoke(final Bookmark bookmark, final List<RegisterMarkerRequest> requests) {
		List<Place> existingPlaces = findExistingPlaces(requests);
		List<Place> newPlaces = findNewPlaces(requests, existingPlaces);

		List<Place> persistedPlaces = placeRepository.saveAll(newPlaces);

		List<Place> places = combineAllPlaces(existingPlaces, persistedPlaces);
		batchCreateMarker.invoke(bookmark, places);
	}

	private List<Place> findExistingPlaces(List<RegisterMarkerRequest> requests) {
		return requests.stream()
			.map(request -> findPlace.invoke(request.title(), request.roadAddress()))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.toList());
	}

	private List<Place> findNewPlaces(List<RegisterMarkerRequest> requests, List<Place> existingPlaces) {
		Set<String> existingPlaceKeys = existingPlaces.stream()
			.map(place -> generateKey(place.getTitle(), place.getRoadAddress()))
			.collect(Collectors.toSet());

		List<Place> newPlaces = new ArrayList<>();
		for (RegisterMarkerRequest request : requests) {
			String requestKey = generateKey(request.title(), request.roadAddress());
			if (!existingPlaceKeys.contains(requestKey)) {
				newPlaces.add(request.toEntity());
			}
		}
		return newPlaces;
	}

	private List<Place> combineAllPlaces(List<Place> existingPlaces, List<Place> persistedNewPlaces) {
		List<Place> allPlaces = new ArrayList<>(existingPlaces);
		allPlaces.addAll(persistedNewPlaces);
		return allPlaces;
	}

	private String generateKey(String title, String roadAddress) {
		return title + KEY_DELIMITER + roadAddress;
	}
}
