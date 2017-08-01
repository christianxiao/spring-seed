package com.profullstack.springseed.core.profile;

import com.profullstack.springseed.core.log.LogbackConfig;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;

public class ProfileApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
	private static final Logger log = LoggerFactory.getLogger(ProfileApplicationContextInitializer.class);

	public static final String OVERRIDE_PROPERTIES_SOURCE_NAME = "override_properties_property_source";
	public static final String OVERRIDE_PROPERTIES_SOURCE_LOCATION = "config.override";

	public static final String PROJECT_PROPERTIES_SOURCE_NAME = "project_properties_property_source";
	public static final String PROJECT_PROPERTIES_SOURCE_LOCATION = "classpath:properties/domain-${" + ACTIVE_PROFILES_PROPERTY_NAME + "}.xml";

	public static final String PROJECT_PROPERTIES_SOURCE_DEFAULT_NAME = "project_properties_property_source";
	public static final String PROJECT_PROPERTIES_SOURCE_DEFAULT_LOCATION = "classpath:properties/domain-default.xml";

	public static final String PARENT_PROPERTIES_SOURCE_NAME = "parent_properties_property_source";
	public static final String PARENT_PROPERTIES_SOURCE_LOCATION = "classpath:properties/core-${" + ACTIVE_PROFILES_PROPERTY_NAME + "}.xml";

	public static final String PARENT_PROPERTIES_SOURCE_DEFAULT_NAME = "parent_properties_property_source_default";
	public static final String PARENT_PROPERTIES_SOURCE_DEFAULT_LOCATION = "classpath:properties/core-default.xml";

	public static final String LOGBACK_CONFIG_LOCATION = "classpath:logback/logback-${" + ACTIVE_PROFILES_PROPERTY_NAME + "}.groovy";


	private ConfigurableEnvironment environment;
	private ResourceLoader resourceLoader;
	private String profile;

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		this.environment = applicationContext.getEnvironment();
		this.resourceLoader = applicationContext;
		this.profile = Profiles.getActiveProfile(environment);

		NameLocationPair pair = getPropertySourceFirst();
		if(pair != null){
			addPropertySource(pair.getName(),pair.getLocation());
		}
		addPropertySource(OVERRIDE_PROPERTIES_SOURCE_NAME, System.getProperty(OVERRIDE_PROPERTIES_SOURCE_LOCATION));
		addPropertySource(PROJECT_PROPERTIES_SOURCE_NAME, PROJECT_PROPERTIES_SOURCE_LOCATION);
		addPropertySource(PROJECT_PROPERTIES_SOURCE_DEFAULT_NAME, PROJECT_PROPERTIES_SOURCE_DEFAULT_LOCATION);
		addPropertySource(PARENT_PROPERTIES_SOURCE_NAME, PARENT_PROPERTIES_SOURCE_LOCATION);
		addPropertySource(PARENT_PROPERTIES_SOURCE_DEFAULT_NAME, PARENT_PROPERTIES_SOURCE_DEFAULT_LOCATION);

		LogbackConfig.init(LOGBACK_CONFIG_LOCATION);
	}

	protected NameLocationPair getPropertySourceFirst(){
		return null;
	}

	private void addPropertySource(String name, String location){
		Properties configurationProperties = getProperties(location);
		PropertiesPropertySource propertySource = new PropertiesPropertySource(name, configurationProperties);
		environment.getPropertySources().addLast(propertySource);
	}

	private Properties getProperties(String location){
		List<String> paths = new ArrayList<>();
		if(location == null || location.length() == 0){
			return new Properties();
		}

		String actualLocation = location.replaceAll("\\$\\{" + ACTIVE_PROFILES_PROPERTY_NAME + "}", profile);
		paths.add(formatPath(actualLocation));

		return loadPropertiesFromPaths(resourceLoader, paths);
	}

	private static String formatPath(String path){
		if (path.contains("classpath:")) {
			return path;
		} else {
			String aPath = new File(path).getAbsolutePath();
			return "file:" + aPath;
		}
	}

	private static Properties loadPropertiesFromPaths(ResourceLoader resourceLoader, List<String> paths) {
		Properties configurationProperties = new Properties();
		for (String path : paths) {
			Resource resource = resourceLoader.getResource(path);
			InputStream is = null;
			try {
				is = resource.getInputStream();
				Properties properties = new Properties();
				properties.loadFromXML(is);
				configurationProperties.putAll(properties);
			} catch (IOException ex) {
				log.error("Failed to load configuration properties. Resource - " + path, ex);
			} finally {
				try {
					if (is != null) {
						is.close();
					}
				} catch (IOException ioe) {
					// ignore
					ioe.printStackTrace();
				}
			}
		}
		return configurationProperties;
	}

	@Data
	public static class NameLocationPair{
		private String name;
		private String location;
		public NameLocationPair(String name, String location){
			this.name = name;
			this.location = location;
		};
	}


}
