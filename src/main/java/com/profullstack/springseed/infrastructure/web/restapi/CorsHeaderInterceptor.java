package com.profullstack.springseed.infrastructure.web.restapi;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by christianxiao on 10/18/16.
 */
public class CorsHeaderInterceptor extends HandlerInterceptorAdapter {

	public boolean preHandle(HttpServletRequest request,
							 HttpServletResponse response, Object handler) throws Exception {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", request.getMethod());
		response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
		response.setHeader("Access-Control-Max-Age", "0");

		if(request.getMethod().equalsIgnoreCase("OPTIONS")){
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			return false;
		}

		return true;
	}
}
