package com.profullstack.springseed.core.redis;


public class ExpirableCacheException extends Exception {

	public ExpirableCacheException(String message) {
		super(message);
	}

	public ExpirableCacheException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExpirableCacheException() {
	}
}
