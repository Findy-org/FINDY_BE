package org.findy.findy_be.place.application.register;

import java.util.List;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.place.dto.request.RegisterPlaceRequest;

public interface BatchRegisterPlace {
	void invoke(final Bookmark bookmark, final List<RegisterPlaceRequest> requests);
}
