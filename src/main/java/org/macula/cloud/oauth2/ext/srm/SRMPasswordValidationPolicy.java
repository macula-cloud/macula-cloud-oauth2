package org.macula.cloud.oauth2.ext.srm;

import org.macula.cloud.oauth2.domain.OAuth2User;
import org.macula.cloud.oauth2.policy.PasswordValidationPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SRMPasswordValidationPolicy implements PasswordValidationPolicy {

	@Autowired
	private SRMUserDetailService srmUserDetailService;

	@Override
	public boolean support(String source) {
		return "SRM".equalsIgnoreCase(source);
	}

	@Override
	public boolean validate(OAuth2User user, String presentedPassword) {
		return srmUserDetailService.loadUserByUsername(user.getUsername(), presentedPassword);
	}

}
