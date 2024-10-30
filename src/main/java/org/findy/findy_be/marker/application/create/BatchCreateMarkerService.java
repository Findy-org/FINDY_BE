package org.findy.findy_be.marker.application.create;

import java.util.ArrayList;
import java.util.List;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.marker.domain.Marker;
import org.findy.findy_be.marker.repository.MarkerRepository;
import org.findy.findy_be.place.domain.Place;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BatchCreateMarkerService implements BatchCreateMarker {

	private final MarkerRepository markerRepository;

	@Override
	public void invoke(final Bookmark bookmark, final List<Place> places) {
		List<Marker> newMarkers = new ArrayList<>();
		places.forEach(place -> {
			if (markerRepository.findByBookmarkAndPlace(bookmark, place).isEmpty()) {
				Marker newMarker = Marker.create(bookmark, place);
				newMarkers.add(newMarker);
			}
		});

		markerRepository.bulkInsert(newMarkers);
	}
}
