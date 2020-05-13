package org.macula.cloud.oauth2.ext.adfs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.infinitusint.emp.sdk.adfs.AdfsSsoServiceFacade;
import com.infinitusint.emp.sdk.adfs.api.SsoServiceFacade;

@Configuration
public class AdfsConfiguration {

	@Bean
	@ConfigurationProperties(prefix = "adfs")
	public AdfsProperties adfsConfig() {
		return new AdfsProperties();
	}

	@Bean
	public SsoServiceFacade ssoServiceFacade(AdfsProperties adfsConfig) {
		return new AdfsSsoServiceFacade(adfsConfig);
	}

}
