package org.macula.cloud.oauth2.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "MC_USER")
public class OAuth2User extends org.macula.cloud.core.domain.OAuth2User implements Serializable {

	private static final long serialVersionUID = 1L;

}
