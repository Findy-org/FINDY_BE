package org.findy.findy_be.marker.application.find;

import org.findy.findy_be.marker.dto.response.PagedMarkerResponse;

public interface FindAllPagedMarkers {

	PagedMarkerResponse invoke(final String userId, final Long bookmarkId, final Long cursor,
		final int size);
}
