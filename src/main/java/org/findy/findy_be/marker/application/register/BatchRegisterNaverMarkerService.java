package org.findy.findy_be.marker.application.register;

import static org.findy.findy_be.place.utils.CategoryResolver.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.marker.application.create.BatchCreateMarker;
import org.findy.findy_be.marker.dto.request.RegisterNaverMarkerRequest;
import org.findy.findy_be.place.application.find.FindPlace;
import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.domain.vo.Category;
import org.findy.findy_be.place.repository.PlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BatchRegisterNaverMarkerService implements BatchRegisterMarker<RegisterNaverMarkerRequest> {

	private final FindPlace findPlace;
	private final PlaceRepository placeRepository;
	private final BatchCreateMarker<RegisterNaverMarkerRequest> batchCreateMarker;

	@Override
	public void invoke(final Bookmark bookmark, final List<RegisterNaverMarkerRequest> requests) {
		List<Place> existingPlaces = findExistingPlaces(requests);
		List<Place> newPlaces = findNewPlaces(requests, existingPlaces);
		List<Place> persistedPlaces = placeRepository.saveAll(newPlaces);

		List<Place> allPlaces = combineAllPlaces(existingPlaces, persistedPlaces);
		batchCreateMarker.invoke(bookmark, allPlaces, requests);
	}

	private List<Place> findExistingPlaces(List<RegisterNaverMarkerRequest> requests) {
		return requests.stream()
			.map(request -> {
				Category category = resolveCategory(request.category() + ">" + request.category());
				return findPlace.invoke(request.title(), request.address(), category);
			})
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.toList());
	}

	private List<Place> findNewPlaces(List<RegisterNaverMarkerRequest> requests, List<Place> existingPlaces) {
		Set<Place> existingPlaceSet = new HashSet<>(existingPlaces);

		return requests.stream()
			.map(RegisterNaverMarkerRequest::toPlaceEntity)
			.filter(place -> !existingPlaceSet.contains(place))
			.toList();
	}

	private List<Place> combineAllPlaces(List<Place> existingPlaces, List<Place> persistedNewPlaces) {
		List<Place> allPlaces = new ArrayList<>(existingPlaces);
		allPlaces.addAll(persistedNewPlaces);
		return allPlaces;
	}
}