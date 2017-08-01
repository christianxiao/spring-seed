package com.profullstack.springseed.core.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by christianxiao on 6/11/17.
 */
public class ErrorCodeException extends Exception {

	@Getter
	@Setter
    private String errorCode;

	public ErrorCodeException(String message, Throwable cause, String errorCode){
		super(message, cause);
		this.errorCode = errorCode;
	}
}
