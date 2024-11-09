package org.findy.findy_be.place.dto.request;

import org.findy.findy_be.place.domain.MajorCategory;
import org.findy.findy_be.place.domain.MiddleCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CategoryRequest(
	@NotNull(message = "대분류는 비어있을 수 없습니다.")
	@Schema(description = "대분류", example = "RESTAURANT")
	MajorCategory majorCategory,

	@Schema(description = "중분류", example = "KOREAN")
	MiddleCategory middleCategory
) {
}
