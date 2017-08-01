package com.profullstack.springseed.core.jpa;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by christianxiao on 7/26/17.
 */
@Getter
@Setter
public class Dbcp2DataSourceFactoryBean implements FactoryBean<DataSource> {

	private String prefix;

	@Autowired
	private Environment environment;

	private BasicDataSource basicDataSource;


	@Override
	public DataSource getObject() throws Exception {
		DataSource dataSource = JpaBuilderUtil.newDataSource(environment, prefix);
		this.basicDataSource = (BasicDataSource)dataSource;
		return dataSource;
	}

	@Override
	public Class<?> getObjectType() {
		return DataSource.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void close() throws SQLException {
		basicDataSource.close();
	}
}
