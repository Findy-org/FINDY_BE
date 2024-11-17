package org.findy.findy_be.marker.application.register;

import org.findy.findy_be.place.domain.Place;

public record PlaceWithTimestamp(
	Place place,
	String timestamp
) {
}
