package com.profullstack.springseed.infrastructure.web.restapi;

import com.profullstack.springseed.infrastructure.security.SecurityUtil;
import lombok.Data;

import java.util.Random;

/**
 * This is a simple database based solution, you need to store this token to database.
 * Created by christianxiao on 10/17/16.
 */
@Data
public class SimpleAccessToken {
	private long createdAt; //16 char
	private int random; //8 char
	private String loginId;

	private String token;

	public SimpleAccessToken(){}

	public SimpleAccessToken(String loginId){
		this.loginId = loginId;
	}

	public void genToken(){
		this.createdAt = System.currentTimeMillis();
		this.random = Math.abs(new Random().nextInt());
		this.token = String.format("%016x", createdAt) + String.format("%08x", random) + loginId;
	}

	public static String genToken(String loginId){
		SimpleAccessToken simpleAccessToken = new SimpleAccessToken(loginId);
		simpleAccessToken.genToken();
		return simpleAccessToken.hash();

		//String s =  String.format("%016x", System.currentTimeMillis()) + String.format("%08x", Math.abs(new Random().nextInt())) + loginId;
		//return SecurityUtil.md5(s);
	}

	public static SimpleAccessToken fromStrToken(String token){
		SimpleAccessToken at = new SimpleAccessToken();
		at.setToken(token);
		return at;
	}


	public String hash(){
		if(token == null){
			return null;
		}
		return SecurityUtil.sha256(token);
	}
}
