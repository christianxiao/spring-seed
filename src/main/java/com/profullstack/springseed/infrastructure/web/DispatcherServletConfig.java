package com.profullstack.springseed.infrastructure.web;

import lombok.Data;

/**
 * Created by christianxiao on 7/30/17.
 */
@Data
public class DispatcherServletConfig {
	private String name;
	private Class<?> webMvcConfigClass;
	private String mapping;
}
