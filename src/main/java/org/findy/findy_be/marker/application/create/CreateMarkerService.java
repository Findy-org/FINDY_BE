package org.findy.findy_be.marker.application.create;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.marker.domain.Marker;
import org.findy.findy_be.marker.repository.MarkerRepository;
import org.findy.findy_be.place.domain.Place;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateMarkerService implements CreateMarker {

	private final MarkerRepository markerRepository;

	@Override
	public void invoke(final Bookmark bookmark, final Place place) {
		Marker marker = Marker.create(bookmark, place);
		markerRepository.save(marker);
	}
}
