package org.findy.findy_be.auth.api.swagger;

import org.findy.findy_be.auth.dto.request.AuthRequestModel;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Tag(name = "Authentication API", description = "OAuth2 및 일반 로그인 관련 인증 API입니다.")
public interface AuthAPIPresentation {

	@Operation(summary = "일반 로그인", description = "사용자 ID와 비밀번호를 통해 로그인을 처리합니다.", responses = {
		@ApiResponse(responseCode = "200", description = "성공적으로 로그인 및 토큰 발급"),
		@ApiResponse(responseCode = "401", description = "인증 실패")
	})
	String login(HttpServletRequest request, HttpServletResponse response,
		@Valid @RequestBody AuthRequestModel authRequestModel);

	@Operation(summary = "액세스 토큰 갱신", description = "Refresh Token을 사용하여 만료된 액세스 토큰을 갱신합니다.", responses = {
		@ApiResponse(responseCode = "200", description = "성공적으로 토큰 갱신됨"),
		@ApiResponse(responseCode = "401", description = "인증 실패 - 잘못된 또는 만료된 토큰")
	})
	void refreshToken(HttpServletRequest request, HttpServletResponse response);
}