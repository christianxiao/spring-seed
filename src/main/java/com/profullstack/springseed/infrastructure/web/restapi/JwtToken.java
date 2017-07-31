package com.profullstack.springseed.infrastructure.web.restapi;


import lombok.Data;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by christianxiao on 10/17/16.
 */
@Data
public class JwtToken {
	private Map<String, String> header = JwtTokenFactory.HEADER_MAP;
	private Map<String, Object> payload = new TreeMap<>();
	private String signature; //base64 encoded

	public JwtToken(){}
}
