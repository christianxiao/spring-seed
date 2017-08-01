package com.profullstack.springseed.sample.batch;

import com.profullstack.springseed.core.profile.ProfileApplicationContextInitializer;
import com.profullstack.springseed.sample.batch.config.BatchContextConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by christianxiao on 7/22/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(classes = BatchContextConfig.class, initializers = ProfileApplicationContextInitializer.class)
public abstract class BatchAbstractTest {
}
