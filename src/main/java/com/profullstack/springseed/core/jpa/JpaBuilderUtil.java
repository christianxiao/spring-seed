package com.profullstack.springseed.core.jpa;

import com.profullstack.springseed.core.utils.BeanPropertyUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by christianxiao on 7/26/17.
 */
public class JpaBuilderUtil {

	private static final String URL = ".url";

	public static DataSource newDataSource(Environment environment, String prefix){
		BasicDataSource basicDataSource = new BasicDataSource();
		BeanPropertyUtil.setBeanProperty(basicDataSource, (AbstractEnvironment)environment, prefix);
		return basicDataSource;
	}

	public static EntityManagerFactory newEntityManagerFactory(DataSource dataSource, Class<?> ... entityPackages){
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);

		BasicDataSource basicDataSource = (BasicDataSource)dataSource;
		Database jpaDatabase = DatabaseType.findTypeByJdbcUrl(basicDataSource.getUrl()).getJpaDatabase();
		vendorAdapter.setDatabase(jpaDatabase);

		HashMap<String, Object> properties = new HashMap<String, Object>();
		//properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		//import @Entity classes
		factory.setPackagesToScan(Arrays.stream(entityPackages).map(thing -> thing.getPackage().getName()).toArray(String[]::new));
		//factory.setPersistenceUnitName("jproject");
		factory.setDataSource(dataSource);
		factory.setJpaPropertyMap(properties);
		factory.afterPropertiesSet();

		return factory.getObject();
	}

	public static PlatformTransactionManager newPlatformTransactionManager(EntityManagerFactory entityManagerFactory){
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory);
		return txManager;
	}

}
