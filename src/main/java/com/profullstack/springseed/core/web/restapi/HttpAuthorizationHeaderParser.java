package com.profullstack.springseed.core.web.restapi;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by christianxiao on 7/23/17.
 * Implementation ref: http://www.ietf.org/rfc/rfc2617.txt
 * schema: Authorization: Digest username="Mufasa",realm="testrealm@host.com" ....
 */
public class HttpAuthorizationHeaderParser {

	public static AuthorizationHeader parseHeader(String header){
		AuthorizationHeader authorizationHeader = new AuthorizationHeader();

		Map<String,String> r = new TreeMap<>();
		header = header.trim();
		int scheme = parseScheme(header);
		authorizationHeader.setScheme(header.substring(0, scheme + 1));

		int kv0 = skipWhitespaces(header, scheme + 1);

		while(true){
			String[] kv = new String[2];
			int kvEnd = parseKeyValue(header, kv0, kv);
			r.put(kv[0], kv[1]);

			int kvSep = header.indexOf(',', kvEnd + 1);
			if(kvSep == -1){
				break;
			}else{
				kv0 = skipWhitespaces(header, kvSep + 1);
			}
		}

		authorizationHeader.setPairs(r);
		return authorizationHeader;
	}

	private static int parseKeyValue(String source, int start, String[] kv){
		int eq = source.indexOf('=', start);
		String key = source.substring(start, eq).trim();
		int value1 = skipWhitespaces(source,eq + 1);

		int value2 = parseValue(source, value1);
		String value = source.substring(value1 + 1, value2);

		kv[0] = key;
		kv[1] = value;
		return value2;
	}

	private static int parseValue(String source, int start){
		int end = start + 1;
		while(end < source.length()){
			if(source.charAt(end) == '"' && source.charAt(end-1) != '\\'){
				return end;
			}else{
				end++;
			}
		}
		return -1;
	}

	private static int parseScheme(String source){
		int end = 0;
		while(end < source.length()){
			if(source.charAt(end) != ' '){
				end ++;
			}else{
				return end -1;
			}
		}
		return -1;
	}

	private static int skipWhitespaces(String source, int start){
		int end = start;
		while(end < source.length()){
			if(source.charAt(end) == ' '){
				end ++;
			}else{
				return end;
			}
		}
		return -1;
	}
}
