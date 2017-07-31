package com.profullstack.springseed.infrastructure.web.restapi;

import com.profullstack.springseed.infrastructure.exception.ErrorCodeException;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by christianxiao on 6/11/17.
 */
public class RestApiException extends ErrorCodeException {
    @Getter
    @Setter
    protected int httpResponseCode = 500;

	public RestApiException(String message, Throwable cause){
		this(message, cause, 500);
	}

	public RestApiException(String message, Throwable cause, int httpResponseCode){
		super(message, cause, null);
		this.httpResponseCode = httpResponseCode;
	}
}
