package org.findy.findy_be.marker.application.create;

import java.util.List;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.marker.dto.request.RegisterMarkerRequest;
import org.findy.findy_be.place.domain.Place;

public interface BatchCreateMarker<T> {
	void invoke(final Bookmark bookmark, final List<Place> places, final List<T> requests);
}
