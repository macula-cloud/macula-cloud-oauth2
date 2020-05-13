package org.macula.cloud.oauth2.config;

import org.macula.cloud.oauth2.authentication.SubjectAuthenticationProvider;
import org.macula.cloud.oauth2.ext.adfs.ADFSAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class GlobalAuthenticationConfigurer extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	private SubjectAuthenticationProvider cloudAuthenticationProvider;

	@Autowired
	private ADFSAuthenticationProvider adfsAuthenticationProvider;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(adfsAuthenticationProvider);
		auth.authenticationProvider(cloudAuthenticationProvider);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
