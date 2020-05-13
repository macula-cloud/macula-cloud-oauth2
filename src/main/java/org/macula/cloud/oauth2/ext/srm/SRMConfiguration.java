package org.macula.cloud.oauth2.ext.srm;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SRMConfiguration {

	@Bean
	@ConfigurationProperties(prefix = "srm")
	public SRMProperties srmConfig() {
		return new SRMProperties();
	}

}
