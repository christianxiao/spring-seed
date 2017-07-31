package com.profullstack.springseed.infrastructure.jpa;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Created by christianxiao on 7/26/17.
 */
public class SpringSeedJpa implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware, BeanFactoryAware {

	private BeanFactory beanFactory;
	private ResourceLoader resourceLoader;
	private Environment environment;

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableSpringSeedJpa.class.getName()));
		String propertyPrefix = attributes.getString("propertyPrefix");
		String beanNamePrefix =  attributes.getString("beanNamePrefix");
		Class<?>[] entityPackages = attributes.getClassArray("baseEntityClasses");

/*
		//There are two ways to register bean dynamically in Spring.
 		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)this.beanFactory;

		DataSource dataSource = JpaBuilderUtil.newDataSource(environment, prefix);
		beanFactory.registerSingleton(prefix + DataSource.class.getSimpleName(), dataSource);

		EntityManagerFactory entityManagerFactory = JpaBuilderUtil.newEntityManagerFactory(dataSource, entityPackages);
		beanFactory.registerSingleton(prefix + "entityManager", entityManagerFactory);

		PlatformTransactionManager platformTransactionManager = JpaBuilderUtil.newPlatformTransactionManager(entityManagerFactory);
		beanFactory.registerSingleton(prefix + "transactionManager", platformTransactionManager);
*/

		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Dbcp2DataSourceFactoryBean.class)
			.addPropertyValue("prefix", propertyPrefix)
			.addPropertyValue("environment", this.environment)
			.setDestroyMethodName("close");
		String dataSourceName = beanNamePrefix.isEmpty()? "dataSource": beanNamePrefix + "DataSource";
		registry.registerBeanDefinition(dataSourceName, builder.getBeanDefinition());

		BeanDefinitionBuilder builderEntity = BeanDefinitionBuilder.genericBeanDefinition(EntityManagerFactoryFactoryBean.class)
			.addPropertyReference("dataSource", dataSourceName)
			.addPropertyValue("entityPackages", entityPackages);
		String entityManagerFactoryName = beanNamePrefix.isEmpty()? "entityManagerFactory": beanNamePrefix + "EntityManagerFactory";
		registry.registerBeanDefinition(entityManagerFactoryName, builderEntity.getBeanDefinition());

		BeanDefinitionBuilder builderTrans = BeanDefinitionBuilder.genericBeanDefinition(JpaTransactionManagerFactoryBean.class)
			.addPropertyReference("entityManagerFactory",entityManagerFactoryName);
		String transactionManagerName = beanNamePrefix.isEmpty()? "transactionManager": beanNamePrefix + "TransactionManager";
		registry.registerBeanDefinition(transactionManagerName, builderTrans.getBeanDefinition());
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
}
