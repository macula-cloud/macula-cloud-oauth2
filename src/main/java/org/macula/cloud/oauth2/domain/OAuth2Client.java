package org.macula.cloud.oauth2.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.macula.cloud.core.domain.Application;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "MC_OAUTH2_CLIENT")
public class OAuth2Client extends Application {

	private static final long serialVersionUID = 1L;

}
