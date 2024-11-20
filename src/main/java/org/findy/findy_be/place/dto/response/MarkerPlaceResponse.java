package org.findy.findy_be.place.dto.response;

import org.findy.findy_be.place.domain.Place;
import org.findy.findy_be.place.domain.vo.Category;

import lombok.Builder;

@Builder
public record MarkerPlaceResponse(
	Long markerId,
	String title,
	String address,
	Category category,
	String mapx,
	String mapy
) {
	public static MarkerPlaceResponse of(final Long markerId, final Place place) {
		return MarkerPlaceResponse.builder()
			.markerId(markerId)
			.title(place.getTitle())
			.address(place.getAddress())
			.category(place.getCategory())
			.mapx(place.getCoordinate().getMapx())
			.mapy(place.getCoordinate().getMapy())
			.build();
	}
}
