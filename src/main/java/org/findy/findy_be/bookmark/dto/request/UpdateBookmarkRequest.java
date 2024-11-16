package org.findy.findy_be.bookmark.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "즐겨찾기 이름 변경 DTO")
public record UpdateBookmarkRequest(

	@NotNull(message = "북마크 이름은 비어있을 수 없습니다.")
	@Schema(description = "북마크 이름", example = "서촌")
	String name
) {
}