package org.findy.findy_be.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "유저 정보 응답 DTO")
public record UserInfoResponse(
	@Schema(description = "유저 이름", type = "string", examples = "나경호")
	String username,

	@Schema(description = "유저 프로필", type = "string", examples = "https://avatars.githubusercontent.com/u/96857599?v=4")
	String profileImageUrl
) {

	public static UserInfoResponse of(final String username, final String profileImageUrl) {
		return UserInfoResponse.builder()
			.username(username)
			.profileImageUrl(profileImageUrl)
			.build();
	}
}
