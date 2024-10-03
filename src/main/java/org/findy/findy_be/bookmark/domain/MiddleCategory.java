package org.findy.findy_be.bookmark.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "즐겨찾기 중분류", enumAsRef = true)
@Getter
@RequiredArgsConstructor
public enum MiddleCategory {

	KOREAN(MajorCategory.RESTAURANT, "한식"),
	CHINESE(MajorCategory.RESTAURANT, "중식"),
	JAPANESE(MajorCategory.RESTAURANT, "일식");

	private final MajorCategory majorCategory;
	private final String label;
}