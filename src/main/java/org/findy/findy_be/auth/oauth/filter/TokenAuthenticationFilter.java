package org.findy.findy_be.auth.oauth.filter;

import java.io.IOException;

import org.findy.findy_be.auth.oauth.token.AuthToken;
import org.findy.findy_be.auth.oauth.token.AuthTokenProvider;
import org.findy.findy_be.common.utils.HeaderUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
	private final AuthTokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
		final FilterChain filterChain) throws ServletException, IOException {

		String tokenStr = HeaderUtil.getAccessToken(request);
		AuthToken token = tokenProvider.convertAuthToken(tokenStr);

		if (token.validate()) {
			Authentication authentication = tokenProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);

	}
}
