package org.findy.findy_be.marker.application.register;

import java.util.List;

import org.findy.findy_be.bookmark.domain.Bookmark;

public interface BatchRegisterMarker<T> {

	void invoke(final Bookmark bookmark, final List<T> requests);
}
