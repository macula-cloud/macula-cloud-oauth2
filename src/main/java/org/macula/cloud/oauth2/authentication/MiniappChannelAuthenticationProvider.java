package org.macula.cloud.oauth2.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class MiniappChannelAuthenticationProvider  implements ChannelAuthentication {

	@Override
	public boolean support(ChannelAuthenticationDetails details) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Authentication loginAuthentication(ChannelAuthenticationDetails details) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

}
