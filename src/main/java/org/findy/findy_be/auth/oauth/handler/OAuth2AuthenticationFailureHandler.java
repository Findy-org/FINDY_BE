package org.findy.findy_be.auth.oauth.handler;

import static org.findy.findy_be.auth.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.*;

import java.io.IOException;

import org.findy.findy_be.auth.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import org.findy.findy_be.common.utils.CookieUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {
		String targetUrl = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
			.map(Cookie::getValue)
			.orElse(("/"));

		exception.printStackTrace();

		targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
			.queryParam("error", exception.getLocalizedMessage())
			.build().toUriString();

		authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
}

