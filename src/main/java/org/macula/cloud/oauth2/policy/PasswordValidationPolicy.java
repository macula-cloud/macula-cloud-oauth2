package org.macula.cloud.oauth2.policy;

import org.macula.cloud.oauth2.domain.OAuth2User;

public interface PasswordValidationPolicy {

	boolean support(String source);

	boolean validate(OAuth2User user, String presentedPassword);
}
