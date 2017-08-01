package com.profullstack.springseed.sample.domain;

import com.profullstack.springseed.core.profile.ProfileApplicationContextInitializer;
import com.profullstack.springseed.sample.domain.config.DomainContextConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Created by christianxiao on 6/25/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(classes = DomainContextConfig.class, initializers = ProfileApplicationContextInitializer.class)
public abstract class DomainAbstractTest {
}
