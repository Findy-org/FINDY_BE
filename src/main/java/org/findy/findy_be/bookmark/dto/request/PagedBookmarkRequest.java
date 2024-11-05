package org.findy.findy_be.bookmark.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record PagedBookmarkRequest(
	@Schema(description = "cursor 기준 bookmarkId (최초 요청 시 null)", example = "0", defaultValue = "0")
	Long cursor,

	@Schema(description = "페이지 크기 (기본값: 10)", example = "10", defaultValue = "10")
	int size
) {
}
