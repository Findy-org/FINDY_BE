package org.findy.findy_be.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeaderUtil {

	private final static String HEADER_AUTHORIZATION = "Authorization";
	private final static String TOKEN_PREFIX = "Bearer ";

	public static String getAccessToken(HttpServletRequest request) {
		String headerValue = request.getHeader(HEADER_AUTHORIZATION);

		if (headerValue == null) {
			log.warn("Authorization header is missing in the request");
			return null;
		}

		if (headerValue.startsWith(TOKEN_PREFIX)) {
			String token = headerValue.substring(TOKEN_PREFIX.length());
			log.debug("Extracted token: {}", token);
			return token;
		} else {
			log.warn("Authorization header does not start with Bearer prefix");
		}

		return null;
	}
}