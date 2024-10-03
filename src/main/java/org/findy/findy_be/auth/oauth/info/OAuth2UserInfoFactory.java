package org.findy.findy_be.auth.oauth.info;

import java.util.Map;

import org.findy.findy_be.auth.oauth.entity.SocialProviderType;
import org.findy.findy_be.auth.oauth.info.implement.KakaoOAuth2UserInfo;
import org.findy.findy_be.auth.oauth.info.implement.NaverOAuth2UserInfo;

public class OAuth2UserInfoFactory {
	public static OAuth2UserInfo getOAuth2UserInfo(SocialProviderType socialProviderType,
		Map<String, Object> attributes) {
		switch (socialProviderType) {
			case NAVER:
				return new NaverOAuth2UserInfo(attributes);
			case KAKAO:
				return new KakaoOAuth2UserInfo(attributes);
			default:
				throw new IllegalArgumentException("Invalid Provider Type.");
		}
	}
}
