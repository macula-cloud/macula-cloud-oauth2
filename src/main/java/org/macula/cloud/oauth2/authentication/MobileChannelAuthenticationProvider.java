package org.macula.cloud.oauth2.authentication;

import org.apache.commons.lang3.StringUtils;
import org.macula.cloud.oauth2.exception.OAuth2AuthenticationException;
import org.macula.cloud.core.principal.SubjectCredential;
import org.macula.cloud.core.principal.SubjectPrincipal;
import org.macula.cloud.core.principal.SubjectPrincipalLoadRepository;
import org.macula.cloud.core.utils.J2CacheUtils;
import org.macula.cloud.core.utils.SecurityUtils;
import org.macula.cloud.security.authentication.CaptchaValidationPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class MobileChannelAuthenticationProvider implements ChannelAuthentication {

	@Autowired
	private SubjectPrincipalLoadRepository principalLoadRepository;

	@Autowired
	private CaptchaValidationPolicy captchaVlidationPolicy;

	@Override
	public boolean support(ChannelAuthenticationDetails details) {
		return details != null && details.getSms() != null;
	}

	@Override
	public Authentication loginAuthentication(ChannelAuthenticationDetails details) throws AuthenticationException {
		if (captchaVlidationPolicy.isNeedCaptchaValidate(details.getLogin())) {
			String captchaCode = details.getCaptchaCode();
			String captcha = details.getCaptcha();
			if (StringUtils.isEmpty(captcha)) {
				throw new OAuth2AuthenticationException("captchaNull");
			}
			if (!captcha.equalsIgnoreCase(captchaCode)) {
				throw new OAuth2AuthenticationException("captchaWrong");
			}
		}

		SubjectPrincipal subjectPrincipal = principalLoadRepository.login(
				SubjectCredential.builder().username(details.getLogin()).password(details.getPassword()).build());

		if (subjectPrincipal == null) {
			throw new OAuth2AuthenticationException("usernameNotFoundOrPasswordIsWrong");
		}

		// login by sms
		if (subjectPrincipal.getMobile().equals(details.getLogin())) {
			String cachedSms = J2CacheUtils.get("sms", subjectPrincipal.getMobile());
			if (cachedSms != null && StringUtils.equals(cachedSms, details.getSms())) {
				throw new OAuth2AuthenticationException("usernameNotFoundOrPasswordIsWrong");
			}
		}

		principalLoadRepository.postValidate(subjectPrincipal);
		return SecurityUtils.cast(subjectPrincipal);
	}

}
