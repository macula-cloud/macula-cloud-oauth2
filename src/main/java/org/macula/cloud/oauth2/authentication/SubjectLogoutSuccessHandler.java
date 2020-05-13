package org.macula.cloud.oauth2.authentication;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.macula.cloud.core.configure.SDKConfigurationProperties;
import org.macula.cloud.core.servlet.SessionIdPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class SubjectLogoutSuccessHandler implements LogoutSuccessHandler {

	@Autowired(required = false)
	private TokenStore tokenStore;

	@Autowired
	private ServletContext servletContext;

	private String loginPath;

	public SubjectLogoutSuccessHandler(SDKConfigurationProperties properties) {
		this.loginPath = properties.getSecurity().getLoginPath();
	}

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {

		SessionIdPayload.extract(request).map(token -> {
			if (tokenStore != null) {
				tokenStore.removeAccessToken(new DefaultOAuth2AccessToken(token));
				tokenStore.removeRefreshToken(new DefaultOAuth2RefreshToken(token));
			}
			return token;
		});
		request.getSession().invalidate();

		String redirectPath = request.getParameter("redirect");
		if (redirectPath != null) {
			response.sendRedirect(redirectPath);
			return;
		}

		String referer = request.getHeader("Referer");
		if (referer != null) {
			response.sendRedirect(referer);
			return;
		}
		response.sendRedirect(servletContext.getContextPath() + loginPath);
	}
}
