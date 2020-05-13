package org.macula.cloud.oauth2.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.macula.cloud.core.domain.AbstractAuditable;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "MC_OAUTH2_CODE")
public class OAuth2Code extends AbstractAuditable<Long> {

	@Column(name = "	CODE", length = 128)
	private String code;

	@JsonIgnore
	@Column(name = "AUTHENTICATION")
	@Lob
	private byte[] authentication;

	@Transient
	private OAuth2Authentication oauth2Authentication;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public byte[] getAuthentication() {
		return authentication;
	}

	public void setAuthentication(byte[] authentication) {
		this.authentication = authentication;
	}

}
