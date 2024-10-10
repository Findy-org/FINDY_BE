package org.findy.findy_be.auth.api;

import java.util.Date;

import org.findy.findy_be.auth.api.swagger.AuthAPIPresentation;
import org.findy.findy_be.auth.dto.AuthRequestModel;
import org.findy.findy_be.auth.oauth.domain.UserPrincipal;
import org.findy.findy_be.auth.oauth.token.AuthToken;
import org.findy.findy_be.auth.oauth.token.AuthTokenProvider;
import org.findy.findy_be.common.config.AppProperties;
import org.findy.findy_be.common.utils.CookieUtil;
import org.findy.findy_be.common.utils.HeaderUtil;
import org.findy.findy_be.user.domain.RoleType;
import org.findy.findy_be.user.domain.UserRefreshToken;
import org.findy.findy_be.user.repository.UserRefreshTokenRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthAPIPresentation {

	private final AppProperties appProperties;
	private final AuthTokenProvider tokenProvider;
	private final AuthenticationManager authenticationManager;
	private final UserRefreshTokenRepository userRefreshTokenRepository;

	private final static long THREE_DAYS_MSEC = 259200000;
	private final static String REFRESH_TOKEN = "refresh_token";

	@GetMapping("/oauth")
	public String handleOAuthRedirect(@RequestParam("token") String token) {
		return "Received token: " + token;
	}

	@PostMapping("/api/auth/login")
	public String login(
		HttpServletRequest request,
		HttpServletResponse response,
		@RequestBody AuthRequestModel authRequestModel
	) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				authRequestModel.getId(),
				authRequestModel.getPassword()
			)
		);

		String userId = authRequestModel.getId();
		SecurityContextHolder.getContext().setAuthentication(authentication);

		Date now = new Date();
		AuthToken accessToken = tokenProvider.createAuthToken(
			userId,
			((UserPrincipal)authentication.getPrincipal()).getRoleType().getCode(),
			new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
		);

		long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
		AuthToken refreshToken = tokenProvider.createAuthToken(
			appProperties.getAuth().getTokenSecret(),
			new Date(now.getTime() + refreshTokenExpiry)
		);

		// userId refresh token 으로 DB 확인
		UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(userId);
		if (userRefreshToken == null) {
			// 없는 경우 새로 등록
			userRefreshToken = new UserRefreshToken(userId, refreshToken.getToken());
			userRefreshTokenRepository.saveAndFlush(userRefreshToken);
		} else {
			// DB에 refresh 토큰 업데이트
			userRefreshToken.setRefreshToken(refreshToken.getToken());
		}

		int cookieMaxAge = (int)refreshTokenExpiry / 60;
		CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
		CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);

		return accessToken.getToken();
	}

	@GetMapping("/api/auth/refresh")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
		// access token 확인
		String accessToken = HeaderUtil.getAccessToken(request);
		AuthToken authToken = tokenProvider.convertAuthToken(accessToken);

		// expired access token 인지 확인
		Claims claims = authToken.getExpiredTokenClaims();

		String userId = claims.getSubject();
		RoleType roleType = RoleType.of(claims.get("role", String.class));

		// refresh token
		String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
			.map(Cookie::getValue)
			.orElse((null));
		AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

		// userId refresh token 으로 DB 확인
		UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserIdAndRefreshToken(userId,
			refreshToken);

		Date now = new Date();
		AuthToken newAccessToken = tokenProvider.createAuthToken(
			userId,
			roleType.getCode(),
			new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
		);

		long validTime = authRefreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

		// refresh 토큰 기간이 3일 이하로 남은 경우, refresh 토큰 갱신
		if (validTime <= THREE_DAYS_MSEC) {
			// refresh 토큰 설정
			long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

			authRefreshToken = tokenProvider.createAuthToken(
				appProperties.getAuth().getTokenSecret(),
				new Date(now.getTime() + refreshTokenExpiry)
			);

			// DB에 refresh 토큰 업데이트
			userRefreshToken.setRefreshToken(authRefreshToken.getToken());

			int cookieMaxAge = (int)refreshTokenExpiry / 60;
			CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
			CookieUtil.addCookie(response, REFRESH_TOKEN, authRefreshToken.getToken(), cookieMaxAge);
		}
	}
}

