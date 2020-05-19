package org.macula.cloud.oauth2.channel;

import org.macula.cloud.core.principal.LoginCredential;
import org.macula.cloud.core.principal.SubjectPrincipal;
import org.springframework.core.Ordered;
import org.springframework.security.core.AuthenticationException;

public interface ChannelAuthentication extends Ordered {

	boolean support(LoginCredential credential);

	SubjectPrincipal loginAuthentication(LoginCredential credential) throws AuthenticationException;
}
