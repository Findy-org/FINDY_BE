package org.findy.findy_be.marker.application.delete;

import static org.findy.findy_be.common.exception.ErrorCode.*;

import org.findy.findy_be.marker.domain.Marker;
import org.findy.findy_be.marker.repository.MarkerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteMarkerService implements DeleteMarker {

	private final MarkerRepository markerRepository;

	public void invoke(String userId, Long markerId) {

		Marker marker = markerRepository.findById(markerId)
			.orElseThrow(
				() -> new EntityNotFoundException(String.format(NOT_FOUND_MARKER_BY_ID.getMessage(), markerId)));
		validateMarkerUser(userId, marker);
		markerRepository.delete(marker);
	}

	private static void validateMarkerUser(final String userId, final Marker marker) {
		if (!marker.getBookmark().getUser().getUserId().equals(userId)) {
			throw new IllegalArgumentException(FORBIDDEN_MARKER_ACCESS.getMessage());
		}
	}
}
