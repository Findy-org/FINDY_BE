package org.findy.findy_be.marker.application.create;

import static org.findy.findy_be.common.exception.ErrorCode.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.marker.domain.Marker;
import org.findy.findy_be.marker.dto.request.RegisterMarkerRequest;
import org.findy.findy_be.marker.repository.MarkerRepository;
import org.findy.findy_be.place.domain.Place;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BatchCreateMarkerService implements BatchCreateMarker<RegisterMarkerRequest> {

	private final MarkerRepository markerRepository;

	@Override
	public void invoke(final Bookmark bookmark, final List<Place> places,
		final List<RegisterMarkerRequest> requests) {
		Set<Long> existingPlaceIds = findExistingPlaceIds(bookmark, places);

		List<Marker> newMarkers = createNewMarkers(bookmark, places, requests, existingPlaceIds);

		saveMarkersAndIncrementCount(bookmark, newMarkers);
	}

	private Set<Long> findExistingPlaceIds(Bookmark bookmark, List<Place> places) {
		return markerRepository.findAllByBookmarkAndPlaces(bookmark, places).stream()
			.map(marker -> marker.getPlace().getId())
			.collect(Collectors.toSet());
	}

	private List<Marker> createNewMarkers(Bookmark bookmark, List<Place> places,
		List<RegisterMarkerRequest> requests, Set<Long> existingPlaceIds) {
		return requests.stream()
			.filter(request -> !existingPlaceIds.contains(
				findPlaceIdByTitleAndRoadAddress(places, request.title(), request.roadAddress())))
			.map(request -> createMarker(bookmark, places, request))
			.toList();
	}

	private Marker createMarker(Bookmark bookmark, List<Place> places, RegisterMarkerRequest request) {
		Place place = findPlaceByTitleAndRoadAddress(places, request.title(), request.roadAddress());
		return Marker.createForYoutubeBookmark(request.timestamp(), bookmark, place);
	}

	private void saveMarkersAndIncrementCount(Bookmark bookmark, List<Marker> newMarkers) {
		markerRepository.saveAll(newMarkers);
		bookmark.incrementMarkersCount(newMarkers.size());
	}

	private Place findPlaceByTitleAndRoadAddress(List<Place> places, String title, String roadAddress) {
		return places.stream()
			.filter(place -> place.getTitle().equals(title) && place.getRoadAddress().equals(roadAddress))
			.findFirst()
			.orElseThrow(
				() -> new EntityNotFoundException(String.format(NOT_FOUND_PLACE.getMessage(), title, roadAddress)));
	}

	private Long findPlaceIdByTitleAndRoadAddress(List<Place> places, String title, String roadAddress) {
		return places.stream()
			.filter(place -> place.getTitle().equals(title) && place.getRoadAddress().equals(roadAddress))
			.map(Place::getId)
			.findFirst()
			.orElse(null);
	}
}
