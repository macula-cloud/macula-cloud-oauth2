package org.macula.cloud.oauth2.channel;

import org.macula.cloud.core.principal.LoginCredential;
import org.macula.cloud.core.principal.SubjectPrincipal;
import org.macula.cloud.oauth2.central.OAuth2UserDetailsService;
import org.macula.cloud.oauth2.exception.OAuth2AuthenticationException;
import org.macula.cloud.security.authentication.CaptchaValidationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class WebChannelAuthenticationProvider implements ChannelAuthentication {

	private final OAuth2UserDetailsService userService;
	private final CaptchaValidationPolicy captchaVlidationPolicy;

	private WebChannelAuthenticationProvider(OAuth2UserDetailsService userService, CaptchaValidationPolicy captchaVlidationPolicy) {
		this.userService = userService;
		this.captchaVlidationPolicy = captchaVlidationPolicy;
	}

	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE + 200;
	}

	@Override
	public boolean support(ChannelAuthenticationDetails details) {
		return details != null && details.getCredential() != null && details.getCredential().getUsername() != null
				&& details.getCredential().getPassword() != null;
	}

	@Override
	public SubjectPrincipal loginAuthentication(ChannelAuthenticationDetails details) throws AuthenticationException {
		LoginCredential credential = details.getCredential();
		if (captchaVlidationPolicy.isNeedCaptchaValidate(credential.getUsername())) {
			String captchaCode = credential.getCaptchaCode();
			String captcha = credential.getCaptcha();
			if (StringUtils.isEmpty(captcha)) {
				throw new OAuth2AuthenticationException("captchaNull");
			}
			if (!captcha.equalsIgnoreCase(captchaCode)) {
				throw new OAuth2AuthenticationException("captchaWrong");
			}
		}
		return userService.loadUserByUsername(credential.getUsername());
	}

}
