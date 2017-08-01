package com.profullstack.springseed.core.jpa;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;

/**
 * Created by christianxiao on 7/27/17.
 */
@Getter
@Setter
public class JpaTransactionManagerFactoryBean implements FactoryBean<PlatformTransactionManager> {

	private EntityManagerFactory entityManagerFactory;

	@Override
	public PlatformTransactionManager getObject() throws Exception {
		return JpaBuilderUtil.newPlatformTransactionManager(entityManagerFactory);
	}

	@Override
	public Class<?> getObjectType() {
		return JpaTransactionManager.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
