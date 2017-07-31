package com.profullstack.springseed.infrastructure.web.restapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.AbstractMap;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by christianxiao on 10/17/16.
 * A simple implementation for jwt.
 * ref: https://en.wikipedia.org/wiki/JSON_Web_Token
 */
public class JwtTokenFactory {

	public static final Map<String,String> HEADER_MAP = Collections.unmodifiableMap(Stream.of(
		new AbstractMap.SimpleEntry<>("alg", "HS256"),
		new AbstractMap.SimpleEntry<>("typ", "JWT"))
		.collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())));
	public static String HEADER_BASE64;

	static {
		try {
			HEADER_BASE64 = encodeBase64(new ObjectMapper().writeValueAsString(HEADER_MAP));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	public static final String DOT = ".";
	public static final String CLAIMS_IAT = "iat";

	private ObjectMapper om = new ObjectMapper();

	private String secret = null;
	private long expMillis = -1;

	public JwtTokenFactory(String secret) {
		this.secret = secret;
	}

	public JwtTokenFactory(String secret, long expMillis) {
		this.secret = secret;
		this.expMillis = expMillis * 1000;
	}

	public String genToken(String payloadKey, String payloadValue){
		JwtToken token = new JwtToken();
		token.getPayload().put(payloadKey, payloadValue);
		return encode(token);
	}

	public String genToken(Map<String,Object> payload){
		JwtToken token = new JwtToken();
		token.getPayload().putAll(payload);
		return encode(token);
	}

	public String encode(JwtToken token) {
		Map<String,Object> payload = token.getPayload();
		if(!payload.containsKey(CLAIMS_IAT)){
			payload.put(CLAIMS_IAT, System.currentTimeMillis());
		}
		try {
			String strPayloadBase64 = encodeBase64(om.writeValueAsString(payload));

			Mac sha256_HMAC = getHmac256();

			String hash = Base64.getEncoder().encodeToString(sha256_HMAC.doFinal((HEADER_BASE64 + DOT + strPayloadBase64).getBytes(Charset.forName("UTF-8"))));
			return HEADER_BASE64 + DOT + strPayloadBase64 + DOT + hash;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean valid(String token){
		String[] tokenSplit = token.split(Pattern.quote(DOT));
		Mac sha256_HMAC = getHmac256();
		String reSig = Base64.getEncoder().encodeToString(sha256_HMAC.doFinal((tokenSplit[0] + DOT + tokenSplit[1]).getBytes(Charset.forName("UTF-8"))));
		if(reSig.equals(tokenSplit[2])){
			return true;
		}
		return false;
	}

	public JwtToken decode(String token){
		String[] tokenSplit = token.split(Pattern.quote(DOT));
		JwtToken gToken = new JwtToken();
		try {
			gToken.setPayload(om.readValue(decodeBase64(tokenSplit[1]), Map.class));
		} catch (IOException e) {
			e.printStackTrace();
		}
		gToken.setSignature(tokenSplit[2]);
		return gToken;
	}

	public String refresh(String token) {

		try {
			String[] tokenSplit = token.split(Pattern.quote(DOT));
			Mac sha256_HMAC = getHmac256();
			String reSig = Base64.getEncoder().encodeToString(sha256_HMAC.doFinal((tokenSplit[0] + DOT + tokenSplit[1]).getBytes(Charset.forName("UTF-8"))));
			if(!reSig.equals(tokenSplit[2])){
				return null;
			}

			Map<String, Object> payload = om.readValue(decodeBase64(tokenSplit[1]), Map.class);
			long iat = Long.parseLong(payload.get(CLAIMS_IAT).toString());
			if(expMillis >= 0 && (System.currentTimeMillis() - iat) > expMillis){
				return null;
			}

			payload.put(CLAIMS_IAT, System.currentTimeMillis());
			JwtToken jToken = new JwtToken();
			jToken.setPayload(payload);

			return encode(jToken);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Mac getHmac256(){
		try {
			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(this.secret.getBytes(Charset.forName("UTF-8")), "HmacSHA256");
			sha256_HMAC.init(secret_key);
			return sha256_HMAC;
		}catch (Exception e){
			//we will never get here
			e.printStackTrace();
			return null;
		}
	}

	private static String encodeBase64(String str) {
		return Base64.getEncoder().encodeToString(str.getBytes(Charset.forName("UTF-8")));
	}

	private static String decodeBase64(String str) {
		return new String(Base64.getDecoder().decode(str), Charset.forName("UTF-8"));
	}
}



