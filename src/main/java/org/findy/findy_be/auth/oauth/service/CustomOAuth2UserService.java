package org.findy.findy_be.auth.oauth.service;

import java.time.LocalDateTime;

import org.findy.findy_be.auth.oauth.domain.SocialProviderType;
import org.findy.findy_be.auth.oauth.domain.UserPrincipal;
import org.findy.findy_be.auth.oauth.info.OAuth2UserInfo;
import org.findy.findy_be.auth.oauth.info.OAuth2UserInfoFactory;
import org.findy.findy_be.common.exception.custom.OAuthProviderMissMatchException;
import org.findy.findy_be.user.domain.RoleType;
import org.findy.findy_be.user.domain.User;
import org.findy.findy_be.user.repository.UserRepository;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User user = super.loadUser(userRequest);

		try {
			return this.process(userRequest, user);
		} catch (AuthenticationException ex) {
			throw ex;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
		}
	}

	private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
		SocialProviderType socialProviderType = SocialProviderType.valueOf(
			userRequest.getClientRegistration().getRegistrationId().toUpperCase());

		OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(socialProviderType, user.getAttributes());
		User savedUser = userRepository.findByUserId(userInfo.getId());

		if (savedUser != null) {
			if (socialProviderType != savedUser.getSocialProviderType()) {
				throw new OAuthProviderMissMatchException(
					"Looks like you're signed up with " + socialProviderType +
						" account. Please use your " + savedUser.getSocialProviderType() + " account to login."
				);
			}
			savedUser.updateUser(userInfo);
		} else {
			savedUser = createUser(userInfo, socialProviderType);
		}

		return UserPrincipal.create(savedUser, user.getAttributes());
	}

	private User createUser(OAuth2UserInfo userInfo, SocialProviderType socialProviderType) {
		LocalDateTime now = LocalDateTime.now();
		User user = User.create(
			userInfo.getId(),
			userInfo.getName(),
			userInfo.getEmail(),
			"Y",
			userInfo.getImageUrl(),
			socialProviderType,
			RoleType.USER,
			now,
			now
		);
		userRepository.saveAndFlush(user);
		return user;
	}
}

