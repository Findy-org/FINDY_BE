package org.findy.findy_be.auth.oauth.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenAccessDeniedHandler implements AccessDeniedHandler {

	private final HandlerExceptionResolver handlerExceptionResolver;

	public TokenAccessDeniedHandler(
		@Qualifier("handlerExceptionResolver") final HandlerExceptionResolver handlerExceptionResolver) {
		this.handlerExceptionResolver = handlerExceptionResolver;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws
		IOException {
		//response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
		handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
	}
}
