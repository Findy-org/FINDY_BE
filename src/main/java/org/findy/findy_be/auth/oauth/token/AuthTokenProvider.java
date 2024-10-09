package org.findy.findy_be.auth.oauth.token;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.findy.findy_be.common.exception.custom.TokenValidFailedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthTokenProvider {

	private final Key key;
	private static final String AUTHORITIES_KEY = "role";

	public AuthTokenProvider(String secret) {
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
	}

	public AuthToken createAuthToken(String id, Date expiry) {
		return new AuthToken(id, expiry, key);
	}

	public AuthToken createAuthToken(String id, String role, Date expiry) {
		return new AuthToken(id, role, expiry, key);
	}

	public AuthToken convertAuthToken(String token) {
		return new AuthToken(token, key);
	}

	public Authentication getAuthentication(AuthToken authToken) {
		if (authToken.validate()) {
			Claims claims = authToken.getTokenClaims();
			String role = claims.get(AUTHORITIES_KEY) != null ? claims.get(AUTHORITIES_KEY).toString() : "ROLE_USER";
			Collection<? extends GrantedAuthority> authorities =
				Arrays.stream(new String[] {role})
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());

			log.debug("claims subject := [{}], role := [{}]", claims.getSubject(), role);
			User principal = new User(claims.getSubject(), "", authorities);

			return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
		} else {
			log.error("Token validation failed");
			throw new TokenValidFailedException();
		}
	}

}

