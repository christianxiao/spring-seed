package com.profullstack.springseed.infrastructure.jpa;

import lombok.Getter;
import org.springframework.orm.jpa.vendor.Database;

import java.sql.Driver;

/**
 * Created by christianxiao on 7/26/17.
 */
@Getter
public enum DatabaseType {
	MYSQL("com.mysql.jdbc.Driver", Constants.MYSQL_VALIDATION_QUERY, Database.MYSQL) {
		@Override
		public boolean acceptUrl(String jdbcUrl) {
			return jdbcUrl.startsWith("jdbc:mysql:");
		}
	},
	ORACLE("oracle.jdbc.driver.OracleDriver", Constants.ORACLE_VALIDATION_QUERY, Database.ORACLE) {
		@Override
		public boolean acceptUrl(String jdbcUrl) {
			return jdbcUrl.startsWith("jdbc:oracle:");
		}
	},
	H2("org.h2.Driver", null, Database.H2) {
		@Override
		public boolean acceptUrl(String jdbcUrl) {
			return jdbcUrl.startsWith("jdbc:h2:");
		}
	},
	LOG4JDBC_MYSQL("net.sf.log4jdbc.sql.jdbcapi.DriverSpy", Constants.MYSQL_VALIDATION_QUERY, Database.MYSQL) {
		@Override
		public boolean acceptUrl(String jdbcUrl) {
			return jdbcUrl.startsWith("jdbc:log4jdbc:mysql:");
		}
	},
	LOG4JDBC_ORACLE("net.sf.log4jdbc.sql.jdbcapi.DriverSpy", Constants.ORACLE_VALIDATION_QUERY, Database.ORACLE) {
		@Override
		public boolean acceptUrl(String jdbcUrl) {
			return jdbcUrl.startsWith("jdbc:log4jdbc:oracle:");
		}
	},
	LOG4JDBC_H2("net.sf.log4jdbc.sql.jdbcapi.DriverSpy", null, Database.H2) {
		@Override
		public boolean acceptUrl(String jdbcUrl) {
			return jdbcUrl.startsWith("jdbc:log4jdbc:h2:");
		}
	};

	private String driverClassName;
	private String pingQuery;
	private Database jpaDatabase;

	DatabaseType(String driverClassName, String pingQuery, Database jpaDatabase) {
		this.driverClassName = driverClassName;
		this.pingQuery = pingQuery;
		this.jpaDatabase = jpaDatabase;
	}

	public abstract boolean acceptUrl(String jdbcUrl);

	@SuppressWarnings("unchecked")
	public Class<? extends Driver> getDriverClass() {
		try {
			return (Class<? extends Driver>) Class.forName(getDriverClassName());
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(getDriverClassName() + " not found.", e);
		}
	}

	public static DatabaseType findTypeByJdbcUrl(String jdbcUrl) {
		for (DatabaseType type : DatabaseType.values()) {
			if (type.acceptUrl(jdbcUrl)) {
				return type;
			}
		}
		throw new IllegalArgumentException(jdbcUrl + " not found.");
	}

	private static class Constants {
		public static final String ORACLE_VALIDATION_QUERY = "/* ping */ select 1 from dual";
		public static final String MYSQL_VALIDATION_QUERY = "/* ping */ select 1";
	}
}

