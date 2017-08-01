package com.profullstack.springseed.core.web.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * Created by christianxiao on 7/30/17.
 */
public class AuthorizationHeaderInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private AuthorizationHeaderRequest authorizationHeaderRequest;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		if (request.getMethod().equalsIgnoreCase(RequestMethod.OPTIONS.name())) {
			return true;
		}

		//parse header
		String header = request.getHeader("Authorization");

		AuthorizationHeader authorizationHeader = null;
		if (header != null) {
			try {
				authorizationHeader = HttpAuthorizationHeaderParser.parseHeader(header);
			} catch (Exception e) {
				e.printStackTrace();
				throw new AuthorizationParseException(e.getMessage(), e);
			}

		}
		authorizationHeaderRequest.setHasHeader(header != null);
		authorizationHeaderRequest.setHeader(header);
		authorizationHeaderRequest.setParsedHeader(authorizationHeader);

		return afterParse(request, response, handler, authorizationHeaderRequest);
	}

	protected boolean afterParse(HttpServletRequest request, HttpServletResponse response, Object handler, AuthorizationHeaderRequest authorizationHeader) throws IOException {
		return true;
	}

	public <A extends Annotation> A getAnnotationFromMethodOrType(HandlerMethod handler, Class<A> annotationType){
		A a = handler.getMethodAnnotation(annotationType);

		//check class level
		if (a == null) {
			return AnnotationUtils.findAnnotation(handler.getBeanType(), annotationType);
		}
		return a;
	}
}
