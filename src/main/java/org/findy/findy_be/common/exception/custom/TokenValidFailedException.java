package org.findy.findy_be.common.exception.custom;

public class TokenValidFailedException extends RuntimeException {

	public TokenValidFailedException() {
		super("Failed to generate Token.");
	}

	private TokenValidFailedException(String message) {
		super(message);
	}
}

