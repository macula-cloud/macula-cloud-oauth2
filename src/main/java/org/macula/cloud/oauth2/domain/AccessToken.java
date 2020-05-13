package org.macula.cloud.oauth2.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.macula.cloud.core.domain.AbstractAuditable;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "MC_OAUTH2_ACCESS_TOKEN")
public class AccessToken extends AbstractAuditable<Long> {

	@Column(name = "	TOKEN_ID", length = 128)
	private String tokenId;

	@Column(name = "AUTHENTICATION_ID", length = 255)
	private String authenticationId;

	@Column(name = "USER_NAME", length = 32)
	private String userName;

	@Column(name = "CLIENT_ID", length = 32)
	private String clientId;

	@Column(name = "REFRESH_TOKEN", length = 128)
	private String refreshToken;

	@JsonIgnore
	@Column(name = "AUTHENTICATION")
	@Lob
	private byte[] authentication;

	@JsonIgnore
	@Column(name = "TOKEN")
	@Lob
	private byte[] token;

	@Transient
	private OAuth2AccessToken oauth2Token;

	@Transient
	private OAuth2Authentication oauth2Authentication;

	public String getAuthenticationId() {
		return authenticationId;
	}

	public void setAuthenticationId(String authenticationId) {
		this.authenticationId = authenticationId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public byte[] getAuthentication() {
		return authentication;
	}

	public void setAuthentication(byte[] authentication) {
		this.authentication = authentication;
	}

	public byte[] getToken() {
		return token;
	}

	public void setToken(byte[] token) {
		this.token = token;
	}

	public OAuth2AccessToken getOauth2Token() {
		if (oauth2Token == null && token != null) {
			oauth2Token = SerializationUtils.deserialize(token);
		}
		return oauth2Token;
	}

	public void setOauth2Token(OAuth2AccessToken oauth2Token) {
		this.oauth2Token = oauth2Token;
		this.token = SerializationUtils.serialize(oauth2Token);
	}

	public OAuth2Authentication getOauth2Authentication() {
		if (oauth2Authentication == null && authentication != null) {
			oauth2Authentication = SerializationUtils.deserialize(authentication);
		}
		return oauth2Authentication;
	}

	public void setOauth2Authentication(OAuth2Authentication oauth2Authentication) {
		this.oauth2Authentication = oauth2Authentication;
		this.authentication = SerializationUtils.serialize(oauth2Authentication);
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String accessToken) {
		this.tokenId = accessToken;
	}

}
