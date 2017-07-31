package com.profullstack.springseed.infrastructure.profile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;

import java.util.Arrays;

import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;

/**
 * Created by christianxiao on 6/11/17.
 */
@Slf4j
public class Profiles {
    public static final String DEFAULT = "default";
    public static final String LOCAL = "local";
    public static final String DEVELOP = "develop";
    public static final String INTEGRATION = "integration";
    public static final String PRODUCTION = "production";

    public static final String DEFAULT_PROFILE = DEFAULT;

    public static final String ACTIVE_PROFILE = getActiveProfile();

    public static String getActiveProfile() {
        String p = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
        String p1 = System.getenv(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
        String active;
        if (p == null) {
            if (p1 == null) {
				System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, DEFAULT);//Used by @Profile
                active = DEFAULT_PROFILE;
            } else {
                active = p1;
            }
        } else {
            active = p;
        }
        if (active == null || active.length() == 0) {
            log.warn(String.format("-D%s=[profile] please set profile name, now default profile used", ACTIVE_PROFILES_PROPERTY_NAME));
            return DEFAULT_PROFILE;
        }
        return active;
    }

    public static String getActiveProfile(Environment environment) {
        String[] activeProfiles = environment.getActiveProfiles();

        if (activeProfiles == null || activeProfiles.length == 0) {
            log.warn(String.format("-D%s=[profile] please set profile name, now default develop profile used", ACTIVE_PROFILES_PROPERTY_NAME));
            return DEFAULT_PROFILE;
        }

        if (activeProfiles.length > 1) {
            throw new IllegalStateException(String.format("more than one profile name is set : %s", Arrays.toString(activeProfiles)));
        }

        return activeProfiles[0];
    }
}
