package org.findy.findy_be.place.application.register;

import org.findy.findy_be.place.dto.request.RegisterSearchedPlaceRequest;

public interface RegisterPlace {
	void invoke(final Long bookmarkId, final RegisterSearchedPlaceRequest request, final String userId);
}
