package org.macula.cloud.oauth2.command;

import org.macula.cloud.oauth2.domain.OAuth2Social;
import org.macula.cloud.oauth2.domain.OAuth2User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSocialUserCommand extends OAuth2User {

	private static final long serialVersionUID = 1L;

	private OAuth2Social oauth2Social;
}
