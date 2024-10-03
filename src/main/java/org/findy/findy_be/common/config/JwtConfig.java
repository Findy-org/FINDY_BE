package org.findy.findy_be.common.config;

import org.findy.findy_be.auth.oauth.token.AuthTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
	@Value("${jwt.secret}")
	private String secret;

	@Bean
	public AuthTokenProvider jwtProvider() {
		return new AuthTokenProvider(secret);
	}
}
