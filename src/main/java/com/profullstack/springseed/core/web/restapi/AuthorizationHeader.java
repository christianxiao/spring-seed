package com.profullstack.springseed.core.web.restapi;

import lombok.Data;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by christianxiao on 7/30/17.
 */
@Data
public class AuthorizationHeader {
	private String scheme;
	private Map<String,String> pairs = new TreeMap<>();
}
