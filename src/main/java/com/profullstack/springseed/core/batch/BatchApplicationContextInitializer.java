package com.profullstack.springseed.core.batch;

import com.profullstack.springseed.core.profile.ProfileApplicationContextInitializer;

import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;

/**
 * Created by christianxiao on 6/11/17.
 */
public class BatchApplicationContextInitializer extends ProfileApplicationContextInitializer {

    public static final String BATCH_PROPERTIES_NAME = "batch_properties_source";
    public static final String BATCH_PROPERTIES_LOCATION = "classpath:properties/batches-${" + ACTIVE_PROFILES_PROPERTY_NAME + "}.xml";


    @Override
    public NameLocationPair getPropertySourceFirst(){
        return new NameLocationPair(BATCH_PROPERTIES_NAME, BATCH_PROPERTIES_LOCATION);
    }
}
