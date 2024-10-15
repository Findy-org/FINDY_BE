package org.findy.findy_be.user.api.swagger;

import org.findy.findy_be.common.meta.LoginUser;
import org.findy.findy_be.user.domain.User;
import org.findy.findy_be.user.dto.UserInfoResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User API", description = "사용자 관련 API 입니다.")
public interface UserAPIPresentation {

	@Operation(summary = "사용자 정보 조회",
		description = "현재 로그인한 사용자의 프로필 이미지 URL을 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공"),
		})
	UserInfoResponse getUser(@LoginUser User user);
}

