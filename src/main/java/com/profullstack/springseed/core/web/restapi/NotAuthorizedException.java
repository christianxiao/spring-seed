package com.profullstack.springseed.core.web.restapi;

/**
 * Created by christianxiao on 7/31/17.
 */
public class NotAuthorizedException extends RestApiException {

	private static final int DEFAULT_HTTP_CODE = 401;
	private static final String DEFAULT_MESSAGE = "You are not authorized.";

	public NotAuthorizedException(String message, Throwable cause){
		super(message, cause, DEFAULT_HTTP_CODE);
	}

	public NotAuthorizedException(Throwable cause){
		super(DEFAULT_MESSAGE, cause, DEFAULT_HTTP_CODE);
	}

}
