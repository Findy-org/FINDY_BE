package org.findy.findy_be.auth.oauth;

import java.util.Map;

import org.findy.findy_be.auth.oauth.userinfo.KakaoOAuth2UserInfo;
import org.findy.findy_be.auth.oauth.userinfo.NaverOAuth2UserInfo;
import org.findy.findy_be.auth.oauth.userinfo.OAuth2UserInfo;
import org.findy.findy_be.user.entity.Role;
import org.findy.findy_be.user.entity.SocialType;
import org.findy.findy_be.user.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {

	private final String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
	private final OAuth2UserInfo oauth2UserInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

	@Builder
	public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
		this.nameAttributeKey = nameAttributeKey;
		this.oauth2UserInfo = oauth2UserInfo;
	}

	public static OAuthAttributes of(SocialType socialType,
		String userNameAttributeName, Map<String, Object> attributes) {

		if (socialType == SocialType.NAVER) {
			return ofNaver(userNameAttributeName, attributes);
		}
		return ofKakao(userNameAttributeName, attributes);
	}

	private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuthAttributes.builder()
			.nameAttributeKey(userNameAttributeName)
			.oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
			.build();
	}

	public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuthAttributes.builder()
			.nameAttributeKey(userNameAttributeName)
			.oauth2UserInfo(new NaverOAuth2UserInfo(attributes))
			.build();
	}

	public User toEntity(SocialType socialType, OAuth2UserInfo oauth2UserInfo) {
		return User.builder()
			.socialType(socialType)
			.socialId(oauth2UserInfo.getId())
			.email(oauth2UserInfo.getEmail())
			.nickname(oauth2UserInfo.getNickname())
			.imageUrl(oauth2UserInfo.getImageUrl())
			.role(Role.GUEST)
			.build();
	}
}
