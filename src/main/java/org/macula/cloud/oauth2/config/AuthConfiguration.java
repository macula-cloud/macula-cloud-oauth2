package org.macula.cloud.oauth2.config;

import org.macula.cloud.oauth2.social.JustAuthAuthenticationProcessingFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AuthRequestProperties.class)
public class AuthConfiguration {

	@Bean
	@ConditionalOnProperty(prefix = "justauth", value = "enabled", havingValue = "true", matchIfMissing = false)
	public AuthRequestFactory authRequestFactory(AuthRequestProperties properties) {
		return new AuthRequestFactory(properties);
	}

	@Bean
	@ConditionalOnProperty(prefix = "justauth", value = "enabled", havingValue = "true", matchIfMissing = false)
	public JustAuthAuthenticationProcessingFilter JustAuthAuthenticationProcessingFilter() {
		return new JustAuthAuthenticationProcessingFilter();
	}

}
