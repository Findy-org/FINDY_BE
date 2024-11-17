package org.findy.findy_be.place.dto.response;

import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.domain.vo.Category;

import lombok.Builder;

@Builder
public record PlaceResponse(
	Long placeId,
	String title,
	String address,
	Category category
) {
	public static PlaceResponse from(final Place place) {
		return PlaceResponse.builder()
			.placeId(place.getId())
			.title(place.getTitle())
			.address(place.getAddress())
			.category(place.getCategory())
			.build();
	}
}
