package com.profullstack.springseed.jproject.web.config;

import com.profullstack.springseed.jproject.web.components.web.Webs;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by christianxiao on 6/11/17.
 */
@Configuration
@ComponentScan(basePackageClasses = {Webs.class})
@EnableWebMvc
@EnableAspectJAutoProxy
public class WebServletConfig extends WebMvcConfigurerAdapter {
}
