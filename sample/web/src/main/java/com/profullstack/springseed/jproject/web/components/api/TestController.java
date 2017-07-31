package com.profullstack.springseed.jproject.web.components.api;

import com.profullstack.springseed.infrastructure.web.restapi.AuthorizationHeaderRequest;
import com.profullstack.springseed.infrastructure.web.restapi.EnableAuthorization;
import com.profullstack.springseed.infrastructure.web.restapi.JwtTokenFactory;
import com.profullstack.springseed.infrastructure.web.restapi.RestApiException;
import com.profullstack.springseed.jproject.domain.components.model.User;
import com.profullstack.springseed.jproject.domain.components.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by christianxiao on 7/24/17.
 */
@Controller
public class TestController {

	@Autowired
	private JwtTokenFactory jwtTokenFactory;

	@Autowired
	private AuthorizationHeaderRequest authorizationHeaderRequest;

	@Autowired
	private UserService userService;

	@EnableAuthorization(false)
	@RequestMapping(value="/hello", method= RequestMethod.GET)
	@ResponseBody
	public String hello(){
		return "hello";
	}

	@EnableAuthorization(false)
	@RequestMapping(value="/login", method= RequestMethod.GET)
	@ResponseBody
	public String login(@RequestParam("loginId") String loginId, @RequestParam("password") String password){
		User user = userService.findUserByLoginIdAndPassword(loginId, password);
		if(user != null){
			return jwtTokenFactory.genToken("loginId", loginId);
		}else{
			return null;
		}
	}

	@RequestMapping(value="/refresh", method= RequestMethod.GET)
	@ResponseBody
	public String refresh(){
		return jwtTokenFactory.refresh((String)authorizationHeaderRequest.getPayload());
	}


	@EnableAuthorization(false)
	@RequestMapping(value="/isLong", method= RequestMethod.GET)
	@ResponseBody
	public Long testException(@RequestParam("number") String number) throws RestApiException {
		Long value;
		try {
			value = Long.parseLong(number);
		} catch (Exception e) {
			throw new RestApiException("Not long value.", e);
		}
		return value;
	}
}
