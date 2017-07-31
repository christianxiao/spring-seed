package com.profullstack.springseed.infrastructure.web.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by christianxiao on 7/30/17.
 */
public class JwtTokenInterceptor extends AuthorizationHeaderInterceptor {
	public static final String KEY_TOKEN = "token";

	@Autowired
	private JwtTokenFactory jwtTokenFactory;

	@Autowired
	private AuthorizationHeaderRequest authorizationHeaderRequest;

	protected boolean afterParse(HttpServletRequest request, HttpServletResponse response, Object handler, AuthorizationHeaderRequest authorizationHeader) throws IOException {
		EnableAuthorization enableAuthorization = super.getAnnotationFromMethodOrType((HandlerMethod) handler, EnableAuthorization.class);
		if(enableAuthorization != null && !enableAuthorization.value()){
			return true;
		}
		if(authorizationHeader.isHasHeader()){
			String token = authorizationHeader.getParsedHeader().getPairs().get(KEY_TOKEN);
			if(jwtTokenFactory.valid(token)){
				authorizationHeaderRequest.setPayload(token);
				return true;
			};
		}
		NotAuthorizedException nouNotAuthorizedException = new NotAuthorizedException(null);
		RestApiErrorResponse restApiErrorResponse = new RestApiErrorResponse(nouNotAuthorizedException);
		restApiErrorResponse.write(response);
		return false;
	}
}
