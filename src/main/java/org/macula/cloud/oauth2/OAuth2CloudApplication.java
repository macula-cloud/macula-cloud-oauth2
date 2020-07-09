package org.macula.cloud.oauth2;

import org.macula.cloud.security.access.EnableSecurityAccess;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringCloudApplication
@EnableWebSecurity
@EnableResourceServer
@EnableSecurityAccess
@EnableJpaRepositories
@EnableJpaAuditing
@EnableScheduling
@EntityScan
@EnableCaching
@EnableFeignClients
public class OAuth2CloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(OAuth2CloudApplication.class, args);
	}

}