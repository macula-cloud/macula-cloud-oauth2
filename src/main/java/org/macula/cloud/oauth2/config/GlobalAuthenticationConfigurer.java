package org.macula.cloud.oauth2.config;

import java.util.List;

import org.macula.cloud.oauth2.central.CentralAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.OrderComparator;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class GlobalAuthenticationConfigurer extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	private List<CentralAuthenticationProvider> channelAuthenticationProviders;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	@Qualifier("auth2RedisHashCacheManager")
	public CacheManager auth2RedisHashCacheManager(CacheManager cacheManager) {
		return cacheManager;
	}

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		OrderComparator.sort(channelAuthenticationProviders);
		for (CentralAuthenticationProvider channelAuthenticationProvider : channelAuthenticationProviders) {
			auth.authenticationProvider(channelAuthenticationProvider);
		}
	}

}
