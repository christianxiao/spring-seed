package com.profullstack.springseed.jproject.web.config;

import com.profullstack.springseed.core.web.restapi.EnableJwtConfig;
import com.profullstack.springseed.core.web.restapi.EnableSpringSeedRestApiMvc;
import com.profullstack.springseed.jproject.web.components.api.Apis;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by christianxiao on 6/11/17.
 */
@Configuration
@ComponentScan(basePackageClasses = {Apis.class})
@EnableSpringSeedRestApiMvc(enableJwtConfig = @EnableJwtConfig(value=true, secretPropertyName="jwt.secret", expiration=10*60),
	enableSwagger2 = true) //replace @EnableWebMvc
public class ApiServletConfig extends WebMvcConfigurerAdapter {
}
