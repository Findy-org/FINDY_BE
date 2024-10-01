package org.findy.findy_be.auth;

import java.util.Map;

import org.findy.findy_be.user.entity.User;

import lombok.Getter;

@Getter
public class UserAdapter extends CustomUserDetails {

	private final User user;
	private Map<String, Object> attributes;

	public UserAdapter(final User user) {
		super(user);
		this.user = user;
	}

	public UserAdapter(final User user, final Map<String, Object> attributes) {
		super(user, attributes);
		this.user = user;
		this.attributes = attributes;
	}
}
