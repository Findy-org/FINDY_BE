package org.findy.findy_be.bookmark.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "즐겨찾기 종류", enumAsRef = true)
@Getter
@RequiredArgsConstructor
public enum BookmarkType {

	YOUTUBE("유튜브에서 불러온 즐겨찾기"),
	CUSTOM("사용자 custom 즐겨찾기");

	private final String description;

}
