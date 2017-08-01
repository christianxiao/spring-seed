package com.profullstack.springseed.jproject.domain.configuration;

import com.profullstack.springseed.core.jpa.EnableSpringSeedJpa;
import com.profullstack.springseed.core.redis.EnableSpringSeedRedis;
import com.profullstack.springseed.core.redis.cacheExpiration;
import com.profullstack.springseed.jproject.domain.components.Domains;
import com.profullstack.springseed.jproject.domain.components.model.Group;
import com.profullstack.springseed.jproject.domain.components.repository.UserRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by christianxiao on 3/24/16.
 */
@Configuration
@ComponentScan(basePackageClasses = {Domains.class})
//Enable JPA support
@EnableTransactionManagement
@EnableSpringSeedJpa(propertyPrefix="jdbc.basicDataSource", baseEntityClasses = {Group.class})
//Enable Spring data jpa
@EnableJpaRepositories(basePackageClasses = { UserRepository.class })
//Enable Redis
@EnableSpringSeedRedis(
	propertyPrefix = "redis.jedisConnectionFactory",
	cacheExpirations = {
		@cacheExpiration(value = "userCache", expiration = 60)
	}
)
public class DomainContextConfig {
}
