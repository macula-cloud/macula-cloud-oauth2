package org.macula.cloud.oauth2.ext.adfs;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import com.infinitusint.emp.sdk.adfs.dto.AccountInfo;

public class ADFSAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 1L;

	private AccountInfo accountInfo;
	private final String username;
	private final String source = "ADFS";

	public ADFSAuthenticationToken(AccountInfo accountInfo) {
		super(null);
		this.accountInfo = accountInfo;
		this.username = accountInfo.getEmployeeID();
	}

	@Override
	public AccountInfo getCredentials() {
		return this.accountInfo;
	}

	@Override
	public String getPrincipal() {
		return this.username;
	}

	public String getUsername() {
		return username;
	}

	public String getSource() {
		return source;
	}

}
