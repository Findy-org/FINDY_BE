package org.findy.findy_be.auth.api.swagger;

import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "Authentication API", description = "OAuth2 및 일반 로그인 관련 인증 API입니다.")
public interface AuthAPIPresentation {

	@Operation(summary = "액세스 토큰 갱신", description = "Refresh Token을 사용하여 만료된 액세스 토큰을 갱신합니다.", responses = {
		@ApiResponse(responseCode = "200", description = "성공적으로 토큰 갱신됨"),
		@ApiResponse(responseCode = "401", description = "인증 실패 - 잘못된 또는 만료된 토큰")
	})
	void refreshToken(HttpServletRequest request, HttpServletResponse response);

	@Operation(summary = "소셜 로그인", description = "소셜 로그인입니다.", responses = {
		@ApiResponse(responseCode = "200", description = "성공적으로 로그인됨"),
	})
	String oauth(@PathVariable("app") String app);
}