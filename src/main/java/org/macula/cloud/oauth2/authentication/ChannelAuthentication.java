package org.macula.cloud.oauth2.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public interface ChannelAuthentication {

	boolean support(ChannelAuthenticationDetails details);
	
	Authentication loginAuthentication(ChannelAuthenticationDetails details) throws AuthenticationException ;
}
