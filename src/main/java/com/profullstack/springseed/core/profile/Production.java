package com.profullstack.springseed.core.profile;

/**
 * Created by christianxiao on 6/11/17.
 */

import org.springframework.context.annotation.Profile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Spring Bean이 특정 프로필에서만 필요할 경우에 이 어노테이션을 달아준다.
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Profile(Profiles.PRODUCTION)
public @interface Production {
}
