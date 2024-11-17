package org.findy.findy_be.marker.application.create;

import java.util.List;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.marker.dto.request.RegisterYouTubeMarkerRequest;
import org.findy.findy_be.place.domain.Place;

public interface BatchCreateMarker {
	void invoke(final Bookmark bookmark, final List<Place> places, final List<RegisterYouTubeMarkerRequest> requests);
}
