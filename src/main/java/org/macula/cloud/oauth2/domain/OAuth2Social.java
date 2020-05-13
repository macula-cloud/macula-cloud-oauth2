package org.macula.cloud.oauth2.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.macula.cloud.core.domain.AbstractPersistable;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "MC_OAUTH2_SOCIAL")
@Getter
@Setter
public class OAuth2Social extends AbstractPersistable<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;

	private String clientId;
	private String socialId;

	// 基础信息
	private String uuid;
	private String unionId;
	private String openId;
	private String nickname;
	private String gender;
	private String language;
	private String city;
	private String province;
	private String country;
	private String avatar;
	private String location;
	private String username;
	private String email;

	// 电话信息
	private String phoneNumber;
	private String purePhoneNumber;
	private String countryCode;

	// 其他信息
	private String company;
	private String blog;
	private String remark;

	// SessionKey
	private String sessionKey;

}
