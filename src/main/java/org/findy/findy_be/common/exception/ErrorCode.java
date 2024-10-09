package org.findy.findy_be.common.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// 400 error

	// 401 error
	UNAUTHORIZED_REQUEST(UNAUTHORIZED, "로그인 후 다시 시도해주세요."),
	LOGIN_FAILURE(UNAUTHORIZED, "로그인 실패! 이메일이나 비밀번호를 확인해주세요."),
	OAUTH_LOGIN_FAILURE(UNAUTHORIZED, "소셜 로그인 실패! 서버 로그를 확인해주세요."),

	// 403 error,
	AUTHENTICATION_EXCEPTION_ERROR(FORBIDDEN, "Authentication Content-Type not supported: %s"),

	// 404 error
	NOT_FOUND_EMAIL(NOT_FOUND, "해당 이메일이 존재하지 않습니다."),
	NOT_FOUND_BOOKMARK_BY_ID(NOT_FOUND, "해당 id : %s의 즐겨찾기가 존재하지 않습니다."),
	NOT_FOUND_USER(NOT_FOUND, "해당 이메일을 가진 유저가 존재하지 않습니다."),
	NOT_FOUND_USER_BY_ID(NOT_FOUND, "해당 id : %s를 가진 유저가 존재하지 않습니다.");

	// 500 error

	private final HttpStatus status;
	private final String message;
}
