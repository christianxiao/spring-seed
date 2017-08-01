package com.profullstack.springseed.core.web;

import com.profullstack.springseed.core.profile.ProfileApplicationContextInitializer;

import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;

/**
 * Created by christianxiao on 6/11/17.
 */
public class WebApplicationContextInitializer extends ProfileApplicationContextInitializer {

    public static final String WEB_PROPERTIES_NAME = "web_properties_source";
    public static final String WEB_PROPERTIES_LOCATION = "classpath:properties/web-${" + ACTIVE_PROFILES_PROPERTY_NAME + "}.xml";

    @Override
    public NameLocationPair getPropertySourceFirst(){
        return new NameLocationPair(WEB_PROPERTIES_NAME, WEB_PROPERTIES_LOCATION);
    }
}
