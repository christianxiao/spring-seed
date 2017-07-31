package com.profullstack.springseed.infrastructure.security;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Created by christianxiao on 4/19/16.
 */
public class SecurityUtil {
	public static final Cache<String, String> md5Cache = CacheBuilder.newBuilder()
		//.maximumSize(4000)
		.build();

	public static final Cache<String, String> sha256Cache = CacheBuilder.newBuilder()
		//.maximumSize(4000)
		.build();

	public static String md5(String password){
		if(password == null){
			return null;
		}
		try {
			return md5Cache.get(password, new Callable<String>() {
				@Override
				public String call(){
					String base64 = null;
					try {
						MessageDigest md = MessageDigest.getInstance("MD5");
						byte[] mdbytes = md.digest(password.getBytes(Charset.forName("UTF-8")));
						base64 = DatatypeConverter.printBase64Binary(mdbytes);
					}catch (NoSuchAlgorithmException e){
						// we will never get this exception
						assert false;
						e.printStackTrace();
					}
					return base64;
				}
			});
		} catch (ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String sha256(String password){
		if(password == null){
			return null;
		}
		try {
			return sha256Cache.get(password, new Callable<String>() {
				@Override
				public String call(){
					String base64 = null;
					try {
						MessageDigest md = MessageDigest.getInstance("SHA-256");
						byte[] mdbytes = md.digest(password.getBytes(Charset.forName("UTF-8")));
						base64 = DatatypeConverter.printBase64Binary(mdbytes);
					}catch (NoSuchAlgorithmException e){
						// we will never get this exception
						assert false;
						e.printStackTrace();
					}
					return base64;
				}
			});
		} catch (ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}
}
