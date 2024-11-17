package org.findy.findy_be.marker.application.register;

import org.findy.findy_be.marker.dto.request.RegisterSearchedMarkerRequest;

public interface RegisterMarker {
	void invoke(final Long bookmarkId, final RegisterSearchedMarkerRequest request, final String userId);
}
