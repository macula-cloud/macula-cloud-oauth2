package org.macula.cloud.oauth2.policy;

import org.macula.cloud.oauth2.domain.OAuth2User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class LocalePasswordValidationPolicy implements PasswordValidationPolicy {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public boolean support(String source) {
		return "NEW".equalsIgnoreCase(source);
	}

	@Override
	public boolean validate(OAuth2User user, String presentedPassword) {
		return passwordEncoder.matches(presentedPassword, user.getPassword());
	}

}
