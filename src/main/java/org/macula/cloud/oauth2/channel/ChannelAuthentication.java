package org.macula.cloud.oauth2.channel;

import org.macula.cloud.core.principal.SubjectPrincipal;
import org.springframework.core.Ordered;
import org.springframework.security.core.AuthenticationException;

public interface ChannelAuthentication extends Ordered {

	boolean support(ChannelAuthenticationDetails details);

	SubjectPrincipal loginAuthentication(ChannelAuthenticationDetails details) throws AuthenticationException;
}
