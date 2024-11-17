package org.findy.findy_be.marker.application.register;

import java.util.List;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.marker.dto.request.RegisterYouTubeMarkerRequest;

public interface BatchRegisterMarker {

	void invoke(final Bookmark bookmark, final List<RegisterYouTubeMarkerRequest> requests);
}
