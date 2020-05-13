package org.macula.cloud.oauth2.ext.adfs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

import com.infinitusint.emp.sdk.adfs.api.SsoServiceFacade;
import com.infinitusint.emp.sdk.adfs.dto.AccountInfo;

@Component
public class ADFSAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	@Autowired
	private SsoServiceFacade ssoServiceFacade;

	public ADFSAuthenticationFilter() {
		super("/login/adfs");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		AccountInfo accountInfo = this.ssoServiceFacade.getAccountInfo(request);
		if (accountInfo != null) {
			ADFSAuthenticationToken token = new ADFSAuthenticationToken(accountInfo);
			return this.getAuthenticationManager().authenticate(token);
		}
		return null;
	}

	@Autowired
	@Override
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}

}
