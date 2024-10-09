package org.findy.findy_be.marker.application.create;

import org.findy.findy_be.bookmark.domain.Bookmark;
import org.findy.findy_be.place.domain.Place;

public interface CreateMarker {

	void invoke(final Bookmark bookmark, final Place place);
}
