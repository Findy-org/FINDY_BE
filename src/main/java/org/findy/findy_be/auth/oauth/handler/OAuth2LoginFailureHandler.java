package org.findy.findy_be.auth.oauth.handler;

import static org.findy.findy_be.common.exception.ErrorCode.*;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws
		IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.getWriter().write(OAUTH_LOGIN_FAILURE.getMessage());
		log.info("소셜 로그인에 실패했습니다. 에러 메시지 : {}", exception.getMessage());
	}
}
