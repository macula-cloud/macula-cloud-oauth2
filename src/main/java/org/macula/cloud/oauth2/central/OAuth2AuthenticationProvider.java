package org.macula.cloud.oauth2.central;

import java.util.List;

import org.macula.cloud.core.principal.LoginCredential;
import org.macula.cloud.core.principal.SubjectPrincipal;
import org.macula.cloud.core.utils.SecurityUtils;
import org.macula.cloud.oauth2.channel.ChannelAuthentication;
import org.macula.cloud.oauth2.channel.ChannelAuthenticationDetails;
import org.macula.cloud.oauth2.exception.OAuth2AuthenticationException;
import org.macula.cloud.oauth2.source.SourceLoginStrategy;
import org.macula.cloud.oauth2.utils.LoginPasswordDecoder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.OrderComparator;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class OAuth2AuthenticationProvider implements CentralAuthenticationProvider {

	private final List<ChannelAuthentication> channelAuthentications;
	private final List<SourceLoginStrategy> sourceLoginStrategies;
	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	public OAuth2AuthenticationProvider(List<ChannelAuthentication> channelAuthentications, List<SourceLoginStrategy> sourceLoginStrategies) {
		this.channelAuthentications = channelAuthentications;
		this.sourceLoginStrategies = sourceLoginStrategies;
		OrderComparator.sort(channelAuthentications);
		OrderComparator.sort(sourceLoginStrategies);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication, () -> messages
				.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports", "Only UsernamePasswordAuthenticationToken is supported"));
		ChannelAuthenticationDetails details = (ChannelAuthenticationDetails) authentication.getDetails();
		LoginCredential credential = details.getCredential();
		if (credential.getUsername() == null) {
			credential.setUsername(authentication.getName());
		}
		credential.setPassword(LoginPasswordDecoder.decode(authentication.getCredentials().toString()));
		return loginAuthentication(details);
	}

	protected Authentication loginAuthentication(ChannelAuthenticationDetails details) throws AuthenticationException {
		for (ChannelAuthentication channelAuthentication : channelAuthentications) {
			if (channelAuthentication.support(details)) {
				SubjectPrincipal principal = channelAuthentication.loginAuthentication(details);
				principal.setCredential(details.getCredential());
				if (principal != null) {
					for (SourceLoginStrategy loginStrategy : sourceLoginStrategies) {
						if (loginStrategy.support(principal.getSource()) && loginStrategy.validate(principal, details.getCredential())) {
							return SecurityUtils.cast(principal);
						}
					}
				}
			}
		}
		throw new OAuth2AuthenticationException("usernameNotFoundOrPasswordIsWrong");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE + 100;
	}

}
