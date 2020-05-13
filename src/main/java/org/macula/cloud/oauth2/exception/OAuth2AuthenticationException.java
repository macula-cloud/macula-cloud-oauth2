package org.macula.cloud.oauth2.exception;

import org.macula.cloud.core.context.CloudApplicationContext;
import org.springframework.security.core.AuthenticationException;

public class OAuth2AuthenticationException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	private String code;

	private final transient Object[] parameters;

	public OAuth2AuthenticationException(String i18nCode, Object... parameters) {
		super(String.format(CloudApplicationContext.getMessage(i18nCode, parameters)));
		this.code = i18nCode;
		this.parameters = parameters;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public String getCode() {
		return code;
	}

}
