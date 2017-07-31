package com.profullstack.springseed.infrastructure.web.restapi;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Created by christianxiao on 6/7/16.
 */
@ControllerAdvice
public class RestApiExceptionAdvice {

	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public void defaultErrorHandler(HttpServletRequest req, HttpServletResponse res, Exception e, Locale locale) throws Exception {
		e.printStackTrace();
		// If the exception is annotated with @ResponseStatus rethrow it and let
		// the framework handle it.
		// AnnotationUtils is a Spring Framework utility class.
		if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
			throw e;

		// Otherwise setup and send the user to a default error-view.
		RestApiException rest;
		if(e instanceof RestApiException) {
			rest = (RestApiException)e;
		}else{
			rest = new RestApiException("Unknown exception occurred. ", e);
		}
		RestApiErrorResponse r = new RestApiErrorResponse(rest);
		r.write(res);
	}
}
