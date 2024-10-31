package org.findy.findy_be.marker.application.create;

import static org.findy.findy_be.common.exception.ErrorCode.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.bookmark.repository.BookmarkRepository;
import org.findy.findy_be.marker.domain.Marker;
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
public class BatchCreateMarkerService implements BatchCreateMarker {

	private final MarkerRepository markerRepository;
	private final BookmarkRepository bookmarkRepository;

	@Override
	public void invoke(final Bookmark bookmark, final List<Place> places) {
		List<Marker> existingMarkers = markerRepository.findAllByBookmarkAndPlaces(bookmark, places);

		Set<Long> existingPlaceIds = getExistingPlaceIds(existingMarkers);
		List<Marker> newMarkers = getNewMarkers(bookmark, places, existingPlaceIds);

		markerRepository.bulkInsert(newMarkers);
		updateMarkersCount(bookmark.getId(), newMarkers);
	}

	private static List<Marker> getNewMarkers(final Bookmark bookmark, final List<Place> places,
		final Set<Long> existingPlaceIds) {
		return places.stream()
			.filter(place -> !existingPlaceIds.contains(place.getId()))
			.map(place -> Marker.create(bookmark, place))
			.collect(Collectors.toList());
	}

	private static Set<Long> getExistingPlaceIds(final List<Marker> existingMarkers) {
		Set<Long> existingPlaceIds;
		existingPlaceIds = existingMarkers.stream()
			.map(marker -> marker.getPlace().getId())
			.collect(Collectors.toSet());
		return existingPlaceIds;
	}

	private void updateMarkersCount(final Long bookmarkId, final List<Marker> newMarkers) {
		Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
			.orElseThrow(() -> new EntityNotFoundException(
				String.format(NOT_FOUND_BOOKMARK_BY_ID.getMessage(), bookmarkId)));
		bookmark.incrementMarkersCount(newMarkers.size());
	}
}
