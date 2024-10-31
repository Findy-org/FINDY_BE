package org.findy.findy_be.common.exception.custom;

public class ForbiddenAccessException extends RuntimeException {

	public ForbiddenAccessException(String message) {
		super(message);
	}
}
