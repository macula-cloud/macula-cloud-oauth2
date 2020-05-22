package org.macula.cloud.oauth2.config;

import org.macula.cloud.oauth2.social.JustAuthAuthenticationProcessingFilter;
import org.macula.cloud.security.access.SecurityAccessPluginConfigure;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class OAuth2SecurityAccessPluginConfigure implements SecurityAccessPluginConfigure {

	private final AuthenticationSuccessHandler authenticationSuccessHandler;
	private final AuthenticationFailureHandler authenticationFailureHandler;
	private final JustAuthAuthenticationProcessingFilter justAuthFilter;

	public OAuth2SecurityAccessPluginConfigure(AuthenticationSuccessHandler authenticationSuccessHandler,
			AuthenticationFailureHandler authenticationFailureHandler, LogoutSuccessHandler logoutSuccessHandler,
			JustAuthAuthenticationProcessingFilter justAuthFilter) {
		this.authenticationSuccessHandler = authenticationSuccessHandler;
		this.authenticationFailureHandler = authenticationFailureHandler;
		this.justAuthFilter = justAuthFilter;
	}

	public void configureAccess(HttpSecurity http) throws Exception {
		if (justAuthFilter != null) {
			if (authenticationSuccessHandler != null) {
				justAuthFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
			}
			if (authenticationFailureHandler != null) {
				justAuthFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
			}
			http.addFilterAfter(justAuthFilter, BasicAuthenticationFilter.class);
		}

	}

}
