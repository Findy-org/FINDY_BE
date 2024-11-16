package org.findy.findy_be.marker.application.find;

import java.util.List;

import org.findy.findy_be.common.dto.pagination.response.SliceResponse;
import org.findy.findy_be.place.dto.response.PlaceResponse;
import org.findy.findy_be.place.repository.PlaceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindAllPagedMarkersService implements FindAllPagedMarkers {

	private final PlaceRepository placeRepository;

	public SliceResponse<PlaceResponse> invoke(final String userId, final Long bookmarkId, final Long cursor,
		final int size) {
		Pageable pageable = PageRequest.of(0, size);
		var placeSlice = placeRepository.findPlacesByUserIdAndBookmarkId(userId, bookmarkId, pageable, cursor);

		List<PlaceResponse> data = placeSlice.stream()
			.map(PlaceResponse::from)
			.toList();
		Long nextCursor = placeSlice.hasNext() ? data.get(data.size() - 1).placeId() : null;

		return new SliceResponse<>(data, cursor != null ? cursor.intValue() : 0, size, placeSlice.hasNext(),
			nextCursor);
	}
}
