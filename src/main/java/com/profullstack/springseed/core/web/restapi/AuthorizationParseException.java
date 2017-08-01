package com.profullstack.springseed.core.web.restapi;

/**
 * Created by christianxiao on 7/30/17.
 */
public class AuthorizationParseException extends RestApiException {

	private static final int DEFAULT_HTTP_CODE = 403;
	private static final String DEFAULT_MESSAGE = "Failed to parse your Authorization header.";

	public AuthorizationParseException(String message, Throwable cause){
		super(message, cause, DEFAULT_HTTP_CODE);
	}

	public AuthorizationParseException(Throwable cause){
		super(DEFAULT_MESSAGE, cause, DEFAULT_HTTP_CODE);
	}

}
