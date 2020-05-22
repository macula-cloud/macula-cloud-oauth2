package org.macula.cloud.oauth2.social;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.macula.cloud.core.principal.LoginCredential;
import org.macula.cloud.core.principal.SubjectPrincipal;
import org.macula.cloud.core.utils.HttpRequestUtils;
import org.macula.cloud.core.utils.J2CacheUtils;
import org.macula.cloud.core.utils.SecurityUtils;
import org.macula.cloud.oauth2.central.OAuth2UserDetailsService;
import org.macula.cloud.oauth2.config.JustAuthRequestFactory;
import org.macula.cloud.oauth2.service.OAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;

@Slf4j
public class JustAuthAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

	@Autowired
	private JustAuthRequestFactory authRequestFactory;

	@Autowired
	private OAuth2UserDetailsService userService;

	@Autowired
	private OAuth2UserService oauth2UserService;

	private List<String> ignoreSources = Arrays.asList("login", "authorize");

	public JustAuthAuthenticationProcessingFilter() {
		super(new AntPathRequestMatcher("/login/**"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		AuthRequest authRequest = getAuthRequest(request);
		AuthCallback authCallback = getAuthCallback(request);

		@SuppressWarnings("unchecked")
		AuthResponse<AuthUser> authResponse = authRequest.login(authCallback);

		String state = authCallback.getState();

		if (state != null) {
			String redirectUrl = J2CacheUtils.get("state", state);
			HttpRequestUtils.setRedirectToAttribute(request, redirectUrl);
		}

		if (authResponse != null) {
			AuthUser user = authResponse.getData();
			if (user != null) {
				LoginCredential credential = new LoginCredential();
				credential.setUsername(user.getUsername());
				credential.setSource(user.getSource().name());
				SubjectPrincipal principal = userService.loadUserByUsername(credential.getUsername());
				if (principal == null) {
					boolean created = oauth2UserService.createAuthUser(user);
					if (created) {
						principal = userService.loadUserByUsername(credential.getUsername());
					}
				}
				if (principal != null) {
					principal.setCredential(credential);
					return SecurityUtils.cast(principal);
				}
			}
		}
		return null;
	}

	@Override
	protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		return super.requiresAuthentication(request, response) && getAuthRequest(request) != null && getAuthCallback(request) != null;
	}

	protected AuthRequest getAuthRequest(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		int index = request.getRequestURI().lastIndexOf("/");
		String source = requestUri.substring(index + 1);
		if (ignoreSources.contains(source)) {
			return null;
		}
		try {
			return authRequestFactory.get(source);
		} catch (Exception ex) {
			log.error("Get AuthRequest from request error: ", ex);
		}
		return null;
	}

	protected AuthCallback getAuthCallback(HttpServletRequest request) {
		AuthCallback callback = new AuthCallback();
		callback.setCode(request.getParameter("code"));
		callback.setAuth_code(request.getParameter("auth_code"));
		callback.setState(request.getParameter("state"));
		return callback.getCode() != null ? callback : null;
	}

	@Override
	public void afterPropertiesSet() {
	}

}
