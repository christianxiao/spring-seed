package com.profullstack.springseed.infrastructure.web.restapi;

import lombok.Data;

/**
 * Created by christianxiao on 7/30/17.
 */
@Data
public class AuthorizationHeaderRequest<T> {

	private boolean hasHeader;
	private String header;
	private AuthorizationHeader parsedHeader;

	private T payload;
}
