package org.findy.findy_be.marker.application.find;

import org.findy.findy_be.common.dto.pagination.response.SliceResponse;
import org.findy.findy_be.place.dto.response.MarkerPlaceResponse;

public interface FindAllPagedMarkers {
	SliceResponse<MarkerPlaceResponse> invoke(final String userId, final Long bookmarkId, final Long cursor,
		final int size);
}
