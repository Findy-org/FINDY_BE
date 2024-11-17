package org.findy.findy_be.common.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// 400 error
	BAD_REQUEST_YOUTUBE_BOOKMARK_REGISTER_ERROR(BAD_REQUEST, "유튜브 즐겨찾기는 장소를 추가할 수 없습니다."),
	BAD_REQUEST_CATEGORY_FORM_ERROR(BAD_REQUEST, "올바른 형식의 Category String이 아닙니다: %s"),
	BAD_REQUEST_BOOKMARK_NAME(BAD_REQUEST, "이미 존재하는 북마크 이름입니다: %s"),

	// 401 error
	UNAUTHORIZED_REQUEST(UNAUTHORIZED, "로그인 후 다시 시도해주세요."),
	LOGIN_FAILURE(UNAUTHORIZED, "로그인 실패! 이메일이나 비밀번호를 확인해주세요."),
	OAUTH_LOGIN_FAILURE(UNAUTHORIZED, "소셜 로그인 실패! 서버 로그를 확인해주세요."),

	// 403 error,
	AUTHENTICATION_EXCEPTION_ERROR(FORBIDDEN, "Authentication Content-Type not supported: %s"),
	FORBIDDEN_BOOKMARK_ACCESS(FORBIDDEN, "해당 즐겨찾기에 접근할 권한이 없습니다."),
	FORBIDDEN_MARKER_ACCESS(FORBIDDEN, "해당 마커에 접근할 권한이 없습니다."),

	// 404 error
	NOT_FOUND_EMAIL(NOT_FOUND, "해당 이메일이 존재하지 않습니다."),
	NOT_FOUND_BOOKMARK_BY_ID(NOT_FOUND, "해당 id : %s의 즐겨찾기가 존재하지 않습니다."),
	NOT_FOUND_MARKER_BY_ID(NOT_FOUND, "해당 id : %s의 마커가 존재하지 않습니다."),
	NOT_FOUND_USER(NOT_FOUND, "해당 이메일을 가진 유저가 존재하지 않습니다."),
	NOT_FOUND_USER_BY_ID(NOT_FOUND, "해당 id : %s를 가진 유저가 존재하지 않습니다."),
	BAD_REQUEST_CATEGORY_NOT_FOUND_ERROR(NOT_FOUND, "해당 input : %s을 가진 카테고리가 없습니다."),
	NOT_FOUND_PLACE(NOT_FOUND, "장소를 찾을 수 없습니다. 장소멸:  %s, 도로명 주소: %s");

	// 500 error

	private final HttpStatus status;
	private final String message;
}
