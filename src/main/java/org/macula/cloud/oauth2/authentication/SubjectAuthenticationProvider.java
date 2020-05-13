package org.macula.cloud.oauth2.authentication;

import java.util.List;

import org.macula.cloud.oauth2.exception.OAuth2AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class SubjectAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private List<ChannelAuthentication> channelAuthentications;

	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication,
				() -> messages.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports",
						"Only UsernamePasswordAuthenticationToken is supported"));

		ChannelAuthenticationDetails details = (ChannelAuthenticationDetails) authentication.getDetails();
		if (details.getLogin() == null) {
			details.setLogin(authentication.getName());
		}
		details.setPassword(SubjectPasswordDecoder.decode(authentication.getCredentials().toString()));

		return loginAuthentication(details);
	}

	protected Authentication loginAuthentication(ChannelAuthenticationDetails details) throws AuthenticationException {

		for (ChannelAuthentication channelAuthentication : channelAuthentications) {
			if (channelAuthentication.support(details)) {
				Authentication authentication = channelAuthentication.loginAuthentication(details);
				if (authentication != null) {
					return authentication;
				}
			}
		}
		throw new OAuth2AuthenticationException("usernameNotFoundOrPasswordIsWrong");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
