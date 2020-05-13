package org.macula.cloud.oauth2.config;

import org.macula.cloud.oauth2.authentication.JustAuthAuthenticationProcessingFilter;
import org.macula.cloud.oauth2.ext.adfs.ADFSAuthenticationFilter;
import org.macula.cloud.security.access.SecurityAccessPluginConfigure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class OAuth2SecurityAccessPluginConfigure implements SecurityAccessPluginConfigure {

	private AuthenticationSuccessHandler authenticationSuccessHandler;
	private AuthenticationFailureHandler authenticationFailureHandler;
	// private LogoutSuccessHandler logoutSuccessHandler;

	@Autowired(required = false)
	private JustAuthAuthenticationProcessingFilter justAuthFilter;

	@Autowired(required = false)
	private ADFSAuthenticationFilter adfsAuthFilter;

	public void init(AuthenticationSuccessHandler authenticationSuccessHandler,
			AuthenticationFailureHandler authenticationFailureHandler, LogoutSuccessHandler logoutSuccessHandler) {
		this.authenticationSuccessHandler = authenticationSuccessHandler;
		this.authenticationFailureHandler = authenticationFailureHandler;
		// this.logoutSuccessHandler = logoutSuccessHandler;
	}

	public void configureAccess(HttpSecurity http) throws Exception {
		if (justAuthFilter != null) {
			if (authenticationSuccessHandler != null) {
				justAuthFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
				adfsAuthFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
			}
			if (authenticationFailureHandler != null) {
				justAuthFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
				adfsAuthFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
			}
			http.addFilterAfter(adfsAuthFilter, BasicAuthenticationFilter.class);
			http.addFilterAfter(justAuthFilter, BasicAuthenticationFilter.class);
		}

	}

}
