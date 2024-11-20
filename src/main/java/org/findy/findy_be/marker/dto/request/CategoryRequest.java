package org.findy.findy_be.marker.dto.request;

import org.findy.findy_be.place.domain.vo.Category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CategoryRequest(
	@NotNull(message = "대분류는 비어있을 수 없습니다.")
	@Schema(description = "대분류", example = "음식점")
	String majorCategory,

	@Schema(description = "중분류", example = "한식")
	String middleCategory
) {
	public Category toEntity() {
		return Category.of(this.majorCategory, this.middleCategory);
	}
}
