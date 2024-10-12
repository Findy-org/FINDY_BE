package org.findy.findy_be.place.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "장소 대분류", enumAsRef = true)
@Getter
@RequiredArgsConstructor
public enum MajorCategory {

	RESTAURANT("음식점"),
	CAFE_AND_DESSERT("카페,디저트"),
	BAR("술집"),
	SHOPPING_AND_DISTRIBUTION("쇼핑,유통"),
	TRAVEL_AND_ATTRACTION("여행,명소"),
	PUBLIC_AND_SOCIAL("공공,사회"),
	HOSPITAL_AND_CLINIC("병원,의원"),
	OTHER("기타");

	private final String label;
}