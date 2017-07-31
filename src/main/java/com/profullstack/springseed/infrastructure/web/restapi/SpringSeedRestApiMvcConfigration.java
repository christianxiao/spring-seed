package com.profullstack.springseed.infrastructure.web.restapi;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by christianxiao on 7/31/17.
 */
@Configuration
@ComponentScan(basePackageClasses = {EnableSpringSeedRestApiMvc.class})
//@EnableWebMvc
@EnableSwagger2
@EnableAspectJAutoProxy
public class SpringSeedRestApiMvcConfigration extends DelegatingWebMvcConfiguration implements ImportAware, EnvironmentAware {

	private Environment environment;

	private boolean disableCors;
	private boolean parseAuthorizationHeader;
	private boolean jwtValue;
	private String jwtSecretPropertyName;
	private long expiration;
	private boolean enableSwagger2;


	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		AnnotationAttributes attributes = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableSpringSeedRestApiMvc.class.getName()));
		this.disableCors =  attributes.getBoolean("disableCors");
		this.parseAuthorizationHeader = attributes.getBoolean("parseAuthorizationHeader");
		AnnotationAttributes jwt = attributes.getAnnotation("enableJwtConfig");
		this.jwtValue = jwt.getBoolean("value");
		this.jwtSecretPropertyName = jwt.getString("secretPropertyName");
		this.expiration = jwt.getNumber("expiration").longValue();

		this.enableSwagger2 = attributes.getBoolean("enableSwagger2");
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public AuthorizationHeaderRequest authorizationHeaderRequest(){
		return new AuthorizationHeaderRequest();
	}

	@Bean
	public JwtTokenFactory jwtTokenFactory(){
		if(jwtValue){
			return new JwtTokenFactory(environment.getRequiredProperty(jwtSecretPropertyName), expiration);
		}
		return null;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		String[] swaggerPaths = {"/swagger-resources/**", "/v2/api-docs"};
		if(!disableCors){
			registry.addInterceptor(new CorsHeaderInterceptor());
		}
		if(parseAuthorizationHeader && !jwtValue) {
			if(enableSwagger2) {
				registry.addInterceptor(authorizationHeaderInterceptor()).excludePathPatterns(swaggerPaths);
			}else{
				registry.addInterceptor(authorizationHeaderInterceptor());
			}
		}
		if(jwtValue) {
			if(enableSwagger2){
				registry.addInterceptor(jwtTokenInterceptor()).excludePathPatterns(swaggerPaths);
			}else{
				registry.addInterceptor(jwtTokenInterceptor());
			}
		}
		super.addInterceptors(registry);
	}

	@Bean
	public AuthorizationHeaderInterceptor authorizationHeaderInterceptor(){
		return new AuthorizationHeaderInterceptor();
	}

	@Bean
	public JwtTokenInterceptor jwtTokenInterceptor(){
		return new JwtTokenInterceptor();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//register swagger ui resources
		if(enableSwagger2) {
			registry.addResourceHandler("/swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");
			registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
		}
		super.addResourceHandlers(registry);
	}
}
