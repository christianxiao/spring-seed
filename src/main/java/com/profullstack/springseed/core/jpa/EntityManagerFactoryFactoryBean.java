package com.profullstack.springseed.core.jpa;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Created by christianxiao on 7/27/17.
 */
@Getter
@Setter
public class EntityManagerFactoryFactoryBean implements FactoryBean<EntityManagerFactory> {

	private DataSource dataSource;
	private Class<?>[] entityPackages;

	@Override
	public EntityManagerFactory getObject() throws Exception {
		return JpaBuilderUtil.newEntityManagerFactory(dataSource, entityPackages);
	}

	@Override
	public Class<?> getObjectType() {
		return EntityManagerFactory.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
