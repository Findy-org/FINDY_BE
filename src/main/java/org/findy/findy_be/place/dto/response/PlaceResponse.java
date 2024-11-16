package org.findy.findy_be.place.dto.response;

import org.findy.findy_be.marker.application.domain.MajorCategory;
import org.findy.findy_be.marker.application.domain.Place;

import lombok.Builder;

@Builder
public record PlaceResponse(
	Long placeId,
	String title,
	String address,
	MajorCategory majorCategory
) {
	public static PlaceResponse from(final Place place) {
		return PlaceResponse.builder()
			.placeId(place.getId())
			.title(place.getTitle())
			.address(place.getAddress())
			.majorCategory(place.getCategory().getMajorCategory())
			.build();
	}
}
