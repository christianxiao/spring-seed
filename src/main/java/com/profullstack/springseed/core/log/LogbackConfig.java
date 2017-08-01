package com.profullstack.springseed.core.log;

import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.ext.spring.LogbackConfigurer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;

/**
 * Created by christianxiao on 7/23/17.
 */
@Slf4j
public class LogbackConfig {

	public static void init(String location) {
		try {
			LogbackConfigurer.initLogging(location);
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					Logger log = LoggerFactory.getLogger(this.getClass());
					log.info("Shutting down logback configuration.");
					LogbackConfigurer.shutdownLogging();
				}
			});
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			log.warn("Can't find logback configuration file. - " + ex.getMessage());

		} catch (JoranException ex) {
			throw new IllegalStateException("Can't load logback configuration file. - " + ex.getMessage(), ex);
		}
	}
}
