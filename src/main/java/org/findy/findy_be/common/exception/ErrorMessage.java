package org.findy.findy_be.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

	YOUTUBE_BOOKMARK_REGISTER_ERROR("유튜브 즐겨찾기는 장소를 추가할 수 없습니다.");

	private final String message;
}
