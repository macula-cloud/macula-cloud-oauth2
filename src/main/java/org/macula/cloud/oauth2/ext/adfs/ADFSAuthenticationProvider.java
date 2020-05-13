package org.macula.cloud.oauth2.ext.adfs;

import org.macula.cloud.oauth2.service.OAuth2UserService;
import org.macula.cloud.core.context.CloudApplicationContext;
import org.macula.cloud.core.event.InstanceProcessEvent;
import org.macula.cloud.core.principal.SubjectCredential;
import org.macula.cloud.core.principal.SubjectPrincipal;
import org.macula.cloud.core.principal.SubjectPrincipalCreatedEvent;
import org.macula.cloud.core.principal.SubjectPrincipalLoadRepository;
import org.macula.cloud.core.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class ADFSAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private SubjectPrincipalLoadRepository principalLoadRepository;

	@Autowired
	private OAuth2UserService userService;

	@Override
	public boolean supports(Class<?> authentication) {
		return ADFSAuthenticationToken.class.isAssignableFrom(authentication);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (!supports(authentication.getClass())) {
			return null;
		}
		ADFSAuthenticationToken adfsToken = (ADFSAuthenticationToken) authentication;
		SubjectCredential credential = SubjectCredential.builder().username(adfsToken.getUsername())
				.source(adfsToken.getSource()).build();

		SubjectPrincipal subjectPrincipal = principalLoadRepository.load(credential);

		if (subjectPrincipal == null) {
			boolean created = userService.createAuthUser(adfsToken.getCredentials());
			if (created) {
				subjectPrincipal = principalLoadRepository.load(credential);
			}
		}

		if (subjectPrincipal != null) {
			principalLoadRepository.postValidate(subjectPrincipal);
			SubjectPrincipalCreatedEvent event = new SubjectPrincipalCreatedEvent(subjectPrincipal.getGuid());
			CloudApplicationContext.getContainer().publishEvent(InstanceProcessEvent.wrap(event));
		}
		return subjectPrincipal == null ? null : SecurityUtils.cast(subjectPrincipal);
	}

}
