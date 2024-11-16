package org.findy.findy_be.place.application.find;

import org.findy.findy_be.common.dto.pagination.response.SliceResponse;
import org.findy.findy_be.place.dto.response.PlaceResponse;

public interface FindAllPagedPlaces {
	SliceResponse<PlaceResponse> invoke(final String userId, final Long bookmarkId, final Long cursor, final int size);
}
