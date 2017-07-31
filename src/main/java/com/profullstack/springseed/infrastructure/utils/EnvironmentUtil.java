package com.profullstack.springseed.infrastructure.utils;

import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.*;

/**
 * Created by christianxiao on 7/29/17.
 */
public class EnvironmentUtil {

	public static Set<String> getAllPropertyNames(AbstractEnvironment env){
		Map<String, Object> map = new HashMap<>();
		for(Iterator it = (env).getPropertySources().iterator(); it.hasNext(); ) {
			org.springframework.core.env.PropertySource propertySource = (org.springframework.core.env.PropertySource) it.next();
			if (propertySource instanceof MapPropertySource) {
				map.putAll(((MapPropertySource) propertySource).getSource());
			}
		}
		return map.keySet();
	}

	public static Map<String,String> getAllProperties(AbstractEnvironment env, String prefix){
		int prefixLength = prefix.length();
		Map<String, Object> map = new TreeMap<>();
		for(Iterator it = (env).getPropertySources().iterator(); it.hasNext(); ) {
			org.springframework.core.env.PropertySource propertySource = (org.springframework.core.env.PropertySource) it.next();
			if (propertySource instanceof MapPropertySource) {
				map.putAll(((MapPropertySource) propertySource).getSource());
			}
		}

		Map<String,String> result = new TreeMap<>();
		for(String name: map.keySet()){
			if(name.startsWith(prefix)){
				result.put(name.substring(prefixLength + 1, name.length()), env.getProperty(name));
			}
		}
		return result;
	}
}
