package org.findy.findy_be.place.repository;

import org.findy.findy_be.place.dto.response.MarkerPlaceResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PlaceRepositoryCustom {

	Slice<MarkerPlaceResponse> findPlacesByUserIdAndBookmarkId(String userId, Long bookmarkId, Pageable pageable,
		Long cursor);
}
