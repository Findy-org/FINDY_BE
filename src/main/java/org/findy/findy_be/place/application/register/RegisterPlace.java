package org.findy.findy_be.place.application.register;

import org.findy.findy_be.place.dto.request.RegisterPlaceRequest;

public interface RegisterPlace {
	void invoke(final String userId, final RegisterPlaceRequest request, final Long bookmarkId);
}
