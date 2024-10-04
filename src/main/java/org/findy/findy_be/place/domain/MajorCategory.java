package org.findy.findy_be.place.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "즐겨찾기 대분류", enumAsRef = true)
@Getter
@RequiredArgsConstructor
public enum MajorCategory {

	RESTAURANT("음식점"),
	TRAVEL_AND_ATTRACTION("여행,명소"),
	SHOPPING_AND_DISTRIBUTION("쇼핑,유통");

	private final String label;
}
