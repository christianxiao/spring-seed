package com.profullstack.springseed.core.web;

import ch.qos.logback.ext.spring.web.WebLogbackConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.util.EnumSet;

import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;


@Order(Ordered.HIGHEST_PRECEDENCE)
public abstract class CommonWebInitializer implements WebApplicationInitializer {
	public static final String LOGBACK_CONFIG_LOCATION_PARAM_VALUE = "classpath:logback/logback-${" + ACTIVE_PROFILES_PROPERTY_NAME + "}.groovy";

	public static final Class<?> APPLICATION_CONTEXT_INITIALIZER_CLASS = WebApplicationContextInitializer.class;

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		//servletContext.setInitParameter("dispatchOptionsRequest", "true"); Not need anymore after spring 4.2
		servletContext.setInitParameter(WebLogbackConfigurer.CONFIG_LOCATION_PARAM, LOGBACK_CONFIG_LOCATION_PARAM_VALUE);

		loadRootApplicationContext(servletContext, getRootContext());
		loadDefaultFilters(servletContext);
		if(getWebServlet() != null)
			addDispatcherServlet(servletContext, "webServlet", getWebServlet(), "/");
		if(getApiServlet() != null)
			addDispatcherServlet(servletContext, "apiServlet", getApiServlet(), "/api/*");
		if(getDispatcherServlets() != null){
			for(DispatcherServletConfig dispatcherServletConfig: getDispatcherServlets()){
				addDispatcherServlet(servletContext, dispatcherServletConfig.getName(), dispatcherServletConfig.getWebMvcConfigClass(), dispatcherServletConfig.getMapping());
			}
		}
	}

	protected abstract Class<?>[] getRootContext();
	protected abstract Class<?> getWebServlet();
	protected abstract Class<?> getApiServlet();

	protected DispatcherServletConfig[] getDispatcherServlets(){
		return null;
	};

	private ApplicationContext loadRootApplicationContext(ServletContext servletContext, Class<?>... configClasses) {
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.setAllowBeanDefinitionOverriding(true);
		rootContext.register(configClasses);

		servletContext.addListener(new ContextLoaderListener(rootContext));

		servletContext.setInitParameter(ContextLoader.CONTEXT_INITIALIZER_CLASSES_PARAM, APPLICATION_CONTEXT_INITIALIZER_CLASS.getName());

		return rootContext;
	}

	protected void loadDefaultFilters(ServletContext servletContext) {
		addEncodingFilter(servletContext);
	}

	private void addEncodingFilter(ServletContext servletContext) {
		FilterRegistration encodingFilter = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);
		encodingFilter.setInitParameter("encoding", "UTF-8");
		encodingFilter.setInitParameter("forceEncoding", "true");
		encodingFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/*");
	}

	private ServletRegistration.Dynamic addDispatcherServlet(ServletContext servletContext, String servletName, Class<?> servletContextConfigClass,
															   String... mappings) {
		return addDispatcherServlet(servletContext, servletName, new Class<?>[] { servletContextConfigClass }, true, mappings);
	}

	private ServletRegistration.Dynamic addDispatcherServlet(ServletContext servletContext, String servletName,
															   Class<?>[] servletContextConfigClasses, boolean allowBeanDefinitionOverriding, String... mappings) {
		Assert.notNull(servletName);
		Assert.notEmpty(servletContextConfigClasses);
		Assert.notEmpty(mappings);

		AnnotationConfigWebApplicationContext servletApplicationContext = new AnnotationConfigWebApplicationContext();
		servletApplicationContext.setAllowBeanDefinitionOverriding(allowBeanDefinitionOverriding);
		servletApplicationContext.register(servletContextConfigClasses);

		ServletRegistration.Dynamic dispatcherServlet = servletContext.addServlet(servletName, new DispatcherServlet(servletApplicationContext));

		dispatcherServlet.setLoadOnStartup(1);
		dispatcherServlet.addMapping(mappings);

		return dispatcherServlet;
	}

}
